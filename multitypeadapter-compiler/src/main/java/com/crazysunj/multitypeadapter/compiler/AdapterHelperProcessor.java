package com.crazysunj.multitypeadapter.compiler;


import com.crazysunj.multitypeadapter.annotation.AdapterHelper;
import com.crazysunj.multitypeadapter.annotation.BindAllLevel;
import com.crazysunj.multitypeadapter.annotation.BindDefaultLevel;
import com.crazysunj.multitypeadapter.annotation.BindEmptyLayoutRes;
import com.crazysunj.multitypeadapter.annotation.BindErrorLayoutRes;
import com.crazysunj.multitypeadapter.annotation.BindHeaderRes;
import com.crazysunj.multitypeadapter.annotation.BindLayoutRes;
import com.crazysunj.multitypeadapter.annotation.BindLevel;
import com.crazysunj.multitypeadapter.annotation.BindLoadingHeaderRes;
import com.crazysunj.multitypeadapter.annotation.BindLoadingLayoutRes;
import com.crazysunj.multitypeadapter.annotation.PreDataCount;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class AdapterHelperProcessor extends AbstractProcessor {

    private Elements mElementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AdapterHelper.class.getCanonicalName());
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(AdapterHelper.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;

            AdapterHelper helperAnno = typeElement.getAnnotation(AdapterHelper.class);
            ClassName listClassName = ClassName.get("java.util", "List");
            ClassName entityClassName = ClassName.bestGuess(helperAnno.entity());
            ParameterizedTypeName entityList = ParameterizedTypeName.get(listClassName, entityClassName);

            MethodSpec.Builder registerModuleBuilder = MethodSpec.methodBuilder("registerModule")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(void.class);
            List<? extends Element> allMembers = mElementUtils.getAllMembers(typeElement);

            Map<String, Map<String, String>> levels = new HashMap<>();
            Map<String, String> headerRess = new HashMap<>();
            Map<String, String> loadingLayoutRess = new HashMap<>();
            Map<String, String> loadingHeaderRess = new HashMap<>();
            Map<String, String> errorLayoutRess = new HashMap<>();
            Map<String, String> emptyLayoutRess = new HashMap<>();
            ExecutableElement preDataCountElement = null;

            for (Element member : allMembers) {
                PreDataCount preDataCountAnno = member.getAnnotation(PreDataCount.class);
                if (member instanceof ExecutableElement && preDataCountAnno != null && preDataCountElement == null) {
                    preDataCountElement = (ExecutableElement) member;
                }
                if (!(member instanceof VariableElement)) {
                    continue;
                }
                VariableElement variableMember = (VariableElement) member;
                BindDefaultLevel defaultTypeAnno = variableMember.getAnnotation(BindDefaultLevel.class);
                if (defaultTypeAnno != null) {
                    Map<String, String> types = new HashMap<>();
                    String type = "0";
                    String level = "0";
                    types.put(type, variableMember.getConstantValue().toString());
                    levels.put(level, types);
                    int headerResId = defaultTypeAnno.headerResId();
                    if (headerResId != 0) {
                        headerRess.put(level, String.valueOf(headerResId));
                    }
                    int loadingLayoutResId = defaultTypeAnno.loadingLayoutResId();
                    if (loadingLayoutResId != 0) {
                        loadingLayoutRess.put(level, String.valueOf(loadingLayoutResId));
                    }
                    int loadingHeaderResId = defaultTypeAnno.loadingHeaderResId();
                    if (loadingHeaderResId != 0) {
                        loadingHeaderRess.put(level, String.valueOf(loadingHeaderResId));
                    }
                    int errorLayoutResId = defaultTypeAnno.errorLayoutResId();
                    if (errorLayoutResId != 0) {
                        errorLayoutRess.put(level, String.valueOf(errorLayoutResId));
                    }
                    int emptyLayoutResId = defaultTypeAnno.emptyLayoutResId();
                    if (emptyLayoutResId != 0) {
                        emptyLayoutRess.put(level, String.valueOf(emptyLayoutResId));
                    }
                    continue;
                }

                BindAllLevel allTypeAnno = variableMember.getAnnotation(BindAllLevel.class);
                if (allTypeAnno != null) {
                    String level = variableMember.getConstantValue().toString();
                    Map<String, String> types = new HashMap<>();
                    int[] type = allTypeAnno.type();
                    int[] layoutResId = allTypeAnno.layoutResId();
                    final int size = type.length;
                    if (size != layoutResId.length) {
                        throw new RuntimeException("please the type and the layoutResId are in agreement !");
                    }
                    for (int i = 0; i < size; i++) {
                        types.put(String.valueOf(type[i]), String.valueOf(layoutResId[i]));
                    }

                    levels.put(level, types);
                    int headerResId = allTypeAnno.headerResId();
                    if (headerResId != 0) {
                        headerRess.put(level, String.valueOf(headerResId));
                    }
                    int loadingLayoutResId = allTypeAnno.loadingLayoutResId();
                    if (loadingLayoutResId != 0) {
                        loadingLayoutRess.put(level, String.valueOf(loadingLayoutResId));
                    }
                    int loadingHeaderResId = allTypeAnno.loadingHeaderResId();
                    if (loadingHeaderResId != 0) {
                        loadingHeaderRess.put(level, String.valueOf(loadingHeaderResId));
                    }
                    int errorLayoutResId = allTypeAnno.errorLayoutResId();
                    if (errorLayoutResId != 0) {
                        errorLayoutRess.put(level, String.valueOf(errorLayoutResId));
                    }
                    int emptyLayoutResId = allTypeAnno.emptyLayoutResId();
                    if (emptyLayoutResId != 0) {
                        emptyLayoutRess.put(level, String.valueOf(emptyLayoutResId));
                    }
                }


                BindLevel typeAnno = variableMember.getAnnotation(BindLevel.class);
                if (typeAnno != null) {
                    String level = variableMember.getConstantValue().toString();
                    Map<String, String> types = new HashMap<>();
                    int[] type = typeAnno.type();
                    int[] layoutResId = typeAnno.layoutResId();
                    final int size = type.length;
                    if (size != layoutResId.length) {
                        throw new RuntimeException("please the type and the layoutResId are in agreement !");
                    }
                    for (int i = 0; i < size; i++) {
                        types.put(String.valueOf(type[i]), String.valueOf(layoutResId[i]));
                    }
                    levels.put(level, types);
                }


                BindLayoutRes layoutResAnno = variableMember.getAnnotation(BindLayoutRes.class);
                if (layoutResAnno != null) {
                    int type = layoutResAnno.type();
                    int level = layoutResAnno.level();
                    Map<String, String> types = new HashMap<>();
                    types.put(String.valueOf(type), variableMember.getConstantValue().toString());
                    levels.put(String.valueOf(level), types);
                }

                BindHeaderRes headerResAnno = variableMember.getAnnotation(BindHeaderRes.class);
                if (headerResAnno != null) {
                    int[] headerLevels = headerResAnno.level();
                    for (int level : headerLevels) {
                        headerRess.put(String.valueOf(level), variableMember.getConstantValue().toString());
                    }
                }

                BindLoadingLayoutRes loadingLayoutResAnno = variableMember.getAnnotation(BindLoadingLayoutRes.class);
                if (loadingLayoutResAnno != null) {
                    int[] loadingLayoutLevels = loadingLayoutResAnno.level();
                    for (int level : loadingLayoutLevels) {
                        loadingLayoutRess.put(String.valueOf(level), variableMember.getConstantValue().toString());
                    }
                }

                BindLoadingHeaderRes loadingHeaderResAnno = variableMember.getAnnotation(BindLoadingHeaderRes.class);
                if (loadingHeaderResAnno != null) {
                    int[] loadingHeaderLevels = loadingHeaderResAnno.level();
                    for (int level : loadingHeaderLevels) {
                        loadingHeaderRess.put(String.valueOf(level), variableMember.getConstantValue().toString());
                    }
                }

                BindErrorLayoutRes errorLayoutResAnno = variableMember.getAnnotation(BindErrorLayoutRes.class);
                if (errorLayoutResAnno != null) {
                    int[] errorLevels = errorLayoutResAnno.level();
                    for (int level : errorLevels) {
                        errorLayoutRess.put(String.valueOf(level), variableMember.getConstantValue().toString());
                    }
                }

                BindEmptyLayoutRes emptyLayoutResAnno = variableMember.getAnnotation(BindEmptyLayoutRes.class);
                if (emptyLayoutResAnno != null) {
                    int[] emptyLevels = emptyLayoutResAnno.level();
                    for (int level : emptyLevels) {
                        emptyLayoutRess.put(String.valueOf(level), variableMember.getConstantValue().toString());
                    }
                }

            }

            for (String level : levels.keySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\n");
                sb.append("registerModule(")
                        .append(level)
                        .append(")\n");

                Map<String, String> types = levels.get(level);
                for (String type : types.keySet()) {
                    sb.append(".type(")
                            .append(type)
                            .append(")\n.layoutResId(")
                            .append(types.get(type))
                            .append(")\n");
                }

                String headerResId = headerRess.get(level);
                if (headerResId != null) {
                    sb.append(".headerResId(")
                            .append(headerResId)
                            .append(")\n");
                }

                String loadingLayoutResId = loadingLayoutRess.get(level);
                String loadingHeaderResId = loadingHeaderRess.get(level);
                if (loadingLayoutResId != null || loadingHeaderResId != null) {
                    sb.append(".loading()\n");
                }

                if (loadingLayoutResId != null) {
                    sb.append(".loadingLayoutResId(")
                            .append(loadingLayoutResId)
                            .append(")\n");
                }

                if (loadingHeaderResId != null) {
                    sb.append(".loadingHeaderResId(")
                            .append(loadingHeaderResId)
                            .append(")\n");
                }

                String errorLayoutResId = errorLayoutRess.get(level);
                if (errorLayoutResId != null) {
                    sb.append(".error()\n")
                            .append(".errorLayoutResId(")
                            .append(errorLayoutResId)
                            .append(")\n");
                }

                String emptyLayoutResId = emptyLayoutRess.get(level);
                if (emptyLayoutResId != null) {
                    sb.append(".empty()\n")
                            .append(".emptyLayoutResId(")
                            .append(emptyLayoutResId)
                            .append(")\n");
                }

                sb.append(".register()");
                registerModuleBuilder.addStatement(sb.toString());
            }

            MethodSpec registerModuleMethod = registerModuleBuilder.build();

            MethodSpec ctorFirst = MethodSpec.constructorBuilder()
                    .addJavadoc("无参构造，默认创建空集合\n")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("super(null)")
                    .build();

            MethodSpec ctorSecond = MethodSpec.constructorBuilder()
                    .addJavadoc("@param data 数据集合\n")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(entityList, "data")
                    .addStatement("super(data)")
                    .build();

            ParameterSpec modeParam = ParameterSpec.builder(ParameterizedTypeName.get(int.class), "mode")
                    .addAnnotation(ClassName.bestGuess("com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper.RefreshMode"))
                    .build();

            MethodSpec ctorThird = MethodSpec.constructorBuilder()
                    .addJavadoc("@param data 数据集合\n")
                    .addJavadoc("@param mode 刷新模式\n")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(entityList, "data")
                    .addParameter(modeParam)
                    .addStatement("super(data, mode)")
                    .build();

            TypeSpec.Builder adapterHelperBuilder = TypeSpec.classBuilder(element.getSimpleName() + "Helper")
                    .superclass(ParameterizedTypeName.get(ClassName.bestGuess(helperAnno.superObj()), entityClassName))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(ctorFirst)
                    .addMethod(ctorSecond)
                    .addMethod(ctorThird)
                    .addMethod(registerModuleMethod);

            if (preDataCountElement != null) {
                MethodSpec preDataCountMethod = MethodSpec.methodBuilder("getPreDataCount")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PROTECTED)
                        .returns(int.class)
                        .addStatement("return mAdapter.$N()", preDataCountElement.getSimpleName())
                        .build();
                adapterHelperBuilder.addMethod(preDataCountMethod);
            }

            adapterHelperBuilder.addJavadoc("apt生成类，最好不要动\n如果有复杂需求可自己项目创建\n");
            TypeSpec adapterHelper = adapterHelperBuilder.build();

            JavaFile.Builder javaFileBuilder = JavaFile.builder(getPackageName(typeElement), adapterHelper);

//            Collection<String> values = typeNames.values();
//            for (String typeName : values) {
//                if ("DEFAULT_VIEW_TYPE".equals(typeName)) {
//                    break;
//                }
//                javaFileBuilder.addStaticImport(ClassName.get(typeElement), typeName);
//            }
            javaFileBuilder.addFileComment("/**\n" +
                    " * Copyright 2017 Sun Jian\n" +
                    " * <p>\n" +
                    " * Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    " * you may not use this file except in compliance with the License.\n" +
                    " * You may obtain a copy of the License at\n" +
                    " * <p>\n" +
                    " * http://www.apache.org/licenses/LICENSE-2.0\n" +
                    " * <p>\n" +
                    " * Unless required by applicable law or agreed to in writing, software\n" +
                    " * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    " * See the License for the specific language governing permissions and\n" +
                    " * limitations under the License.\n" +
                    " */");
            JavaFile javaFile = javaFileBuilder.build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}
