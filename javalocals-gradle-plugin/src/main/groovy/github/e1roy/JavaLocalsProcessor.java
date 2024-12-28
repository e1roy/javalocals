package github.e1roy;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.*;
import spoon.reflect.visitor.CtScanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gerard on 21/10/2014.
 */
public class JavaLocalsProcessor extends AbstractProcessor<CtClass> {

    int count = 0;
    static String fillMethodName = "printLocals";
    static boolean showLineNumber = false;

    public JavaLocalsProcessor() {
//        new RuntimeException().printStackTrace();
    }

    public void setFillMethodName(String fillMethodName) {
        this.fillMethodName = fillMethodName;
    }

    @Override
    public void process(CtClass ctClass) {
        count++;
        ctClass.accept(new VariableCollectorVisitor());
//        System.out.println(ctClass);
    }

    public void processCtClassForTest(CtClass clazz) {
        process(clazz);
    }

    @Override
    public void processingDone() {
        super.processingDone();
    }


    static class VariableCollectorVisitor extends CtScanner {
        private List<CtVariableReference<?>> variablesInScope = new ArrayList<>();
        private boolean isForLoop = false;

        public void addVariable(CtVariableReference<?> var) {
            variablesInScope.add(var);
        }

        @Override
        public <T> void visitCtMethod(CtMethod<T> m) {
            backUpAndRecover(() -> {
                m.getParameters().forEach(param -> addVariable(param.getReference()));
                super.visitCtMethod(m);
            });
        }

        @Override
        public <T> void visitCtBlock(CtBlock<T> block) {
            backUpAndRecover(() -> {
                super.visitCtBlock(block);
            });
        }

        @Override
        public <T> void visitCtLocalVariable(CtLocalVariable<T> localVariable) {
            // 循环遍历, 先访问变量,后访问block,并且block中能访问这个变量.
            super.visitCtLocalVariable(localVariable);
            addVariable(localVariable.getReference());
        }



        @Override
        public <S> void visitCtCase(CtCase<S> caseStatement) {
            backUpAndRecover(() -> super.visitCtCase(caseStatement));
        }

        @Override
        public <S> void visitCtSwitch(CtSwitch<S> switchStatement) {
            backUpAndRecover(() -> super.visitCtSwitch(switchStatement));
        }

        @Override
        public void visitCtFor(CtFor forLoop) {
            var backIsForLoop = this.isForLoop;
            this.isForLoop = true;
            backUpAndRecover(() -> {
                super.visitCtFor(forLoop);
            });
            this.isForLoop = backIsForLoop;
        }

        @Override
        public void visitCtTry(CtTry tryBlock) {
            super.visitCtTry(tryBlock);
        }

        @Override
        public void visitCtTryWithResource(CtTryWithResource tryWithResource) {
            backUpAndRecover(() -> {
                // 不能直接调用super.visitCtTryWithResource(tryWithResource);
                // try的变量在catch中是不可见的, 这里hook一下函数. 临时处理访问. 可以work
//                super.visitCtTryWithResource(tryWithResource);
                enter(tryWithResource);
                scan(CtRole.ANNOTATION, tryWithResource.getAnnotations());
                backUpAndRecover(() -> {
                    scan(CtRole.TRY_RESOURCE, tryWithResource.getResources());
                    scan(CtRole.BODY, tryWithResource.getBody());
                });
                scan(CtRole.CATCH, tryWithResource.getCatchers());
                scan(CtRole.FINALIZER, tryWithResource.getFinalizer());
                scan(CtRole.COMMENT, tryWithResource.getComments());
                exit(tryWithResource);
            });
        }



        @Override
        public void visitCtAnonymousExecutable(CtAnonymousExecutable anonymousExec) {
            /*
            {
                int a = 1;
                printLocals();
            }
            static {
                int staticA = 1;
                printLocals();
            }
             */
            super.visitCtAnonymousExecutable(anonymousExec);
        }

        @Override
        public <T> void visitCtCatchVariableReference(CtCatchVariableReference<T> reference) {
            super.visitCtCatchVariableReference(reference);
        }

        @Override
        public <T> void visitCtCatchVariable(CtCatchVariable<T> catchVariable) {
            addVariable(catchVariable.getReference());
            super.visitCtCatchVariable(catchVariable);
        }

        @Override
        public void visitCtCatch(CtCatch catchBlock) {
            backUpAndRecover(() -> super.visitCtCatch(catchBlock));
        }

        @Override
        public <T> void visitCtInvocation(CtInvocation<T> invocation) {
            if (invocation.getExecutable().getSimpleName().equals(fillMethodName)) {
                // Set arguments to the variables currently in scope
                List<CtExpression<?>> args = new ArrayList<>();
                // 添加当前代码的行号作为第一个参数
                if (showLineNumber) {
                    args.add(invocation.getFactory().Code().createLiteral(invocation.getPosition().getLine()));
                }
                for (CtVariableReference<?> varRef : variablesInScope) {
                    // 获取变量名称
                    String simpleName = varRef.getSimpleName();
                    args.add(invocation.getFactory().Code().createLiteral(simpleName));
                    args.add(invocation.getFactory().Code().createVariableRead(varRef, false));
                }
                invocation.setArguments(args);
            }
            super.visitCtInvocation(invocation);
        }

        @Override
        public void scan(CtRole role, Collection<? extends CtElement> elements) {
            super.scan(role, elements);
        }

        @Override
        public void scan(CtRole role, CtElement element) {
            super.scan(role, element);
        }

        @Override
        public <T> void visitCtLambda(CtLambda<T> lambda) {
            backUpAndRecover(() -> {
                for (CtParameter<?> param : lambda.getParameters()) {
                    addVariable(param.getReference());
                }
                super.visitCtLambda(lambda);
            });
        }

        public void backUpAndRecover(Runnable run) {
            // 备份variablesInScope
            List<CtVariableReference<?>> variablesBeforeBlock = new ArrayList<>(variablesInScope);
            run.run();
            // 恢复variablesInScope
            variablesInScope = variablesBeforeBlock;
        }
    }
}