package com.chen1335.renderjs.ProbeSupport;

import com.chen1335.renderjs.Renderjs;
import dev.latvian.mods.kubejs.script.ScriptType;
import moe.wolfgirl.probejs.events.ProbeEvents;
import moe.wolfgirl.probejs.events.TypingModificationEventJS;
import moe.wolfgirl.probejs.lang.typescript.code.member.ClassDecl;
import moe.wolfgirl.probejs.lang.typescript.code.member.MethodDecl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RenderJSParamFixer {

    private static final Set<Class<?>> classes= new HashSet<>();
    public static void addListener(){
        ProbeEvents.MODIFY_DOC.listenJava(ScriptType.CLIENT, null, eventJS -> {
            TypingModificationEventJS modificationEventJS = (TypingModificationEventJS) eventJS;
            classes.forEach(aClass -> {
                Method[] JavaMethods = aClass.getMethods();
                modificationEventJS.modify(aClass,(typeScriptFile -> {
                    typeScriptFile.findCode(ClassDecl.class).ifPresent(classDecl->{
                        for (MethodDecl methodDecl : classDecl.methods) {
                            Arrays.stream(JavaMethods).filter(method -> method.getName().equals(methodDecl.name)).findFirst().ifPresent(method -> {
                                for (int i = 0; i < method.getParameterTypes().length; i++) {
                                    Annotation[] parameterAnnotation = method.getParameterAnnotations()[i];
                                    int finalI = i;
                                    Renderjs.LOGGER.info(String.valueOf(parameterAnnotation.length));
                                    Arrays.stream(parameterAnnotation).filter(annotation -> annotation instanceof ParamInfo).findFirst().ifPresent(annotation -> {
                                        ParamInfo paramInfo = (ParamInfo) annotation;
                                        methodDecl.params.get(finalI).name = paramInfo.argName();
                                    });
                                }
                            });
                        }
                    });
                }));
            });
            return null;
        });
    }

    public static void addFixClass (Class<?> clazz){
        classes.add(clazz);
    }
}
