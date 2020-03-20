package cn.wang.findview.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.tree.JCTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import cn.wang.findview.annotation.FindView;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/9/11
 */
@AutoService(Processor.class)
public class ViewCompiler extends AbstractProcessor {


    private Messager mMessager;
    private Elements elementUtils;
    private Types typeUtils;
    private List<Integer> mViewIds;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FindView.class);
            if (null != elements && elements.size() > 0) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, "===========开始咯============");

                Iterator<? extends Element> iterator = elements.iterator();
                while (iterator.hasNext()) {
                    Element element = iterator.next();
                    handleElement(element);
                }
            }
        } catch (Exception e) {

        }
        return true;
    }

    private void handleElement(Element element) {
        TypeMirror viewTypeMirror = elementUtils.getTypeElement(ViewCompilerConstant.VIEW_TYPE).asType();
        if(mViewIds == null){
            mViewIds = new ArrayList<>();
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "-----元素是： " + element);
        ElementKind kind = element.getKind();
        //确定我们只要字段类型的
        if (!kind.isField()) {
            return;
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "是 Field字段   " + element);
        //获取该属性所属类元素.
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        //查看属性的限制符。private的访问不到在外部类中。
        if (!canGeneratedCode(element, enclosingElement)) {
            return;
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "且可以访问到 " + element);

        TypeMirror typeMirror = element.asType();
        if (typeMirror.getKind() == TypeKind.TYPEVAR) {
            //是一个属性元素.
            TypeVariable tv = (TypeVariable) typeMirror;
            //获取最上层的元素
            typeMirror = tv.getUpperBound();
        }
        //属性不是View的话就返回.
        if (!typeUtils.isSubtype(typeMirror, viewTypeMirror)) {
            return;
        }
        FindView annotation = element.getAnnotation(FindView.class);
        if (null == annotation) {
            return;
        }
        int value = annotation.value();
        String className = enclosingElement.getSimpleName().toString();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "类型也是View的 " + value+"  simpleName  "+className);
        mViewIds.add(value);

        mMessager.printMessage(Diagnostic.Kind.NOTE, "-----元素的ElementKind 是： " + kind);


    }

    /**
     * 检查三个方面:
     * 1.检查属性是否可访问.只能是public.
     * 2.检查该类是否可访问.只能是public.
     * 3.检查该属性的直接父类必须是class对象.
     *
     * @param element
     * @return
     */
    private boolean canGeneratedCode(Element element, TypeElement typeElement) {
        boolean needContinue = true;

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)) {
            needContinue = false;
        }

        Set<Modifier> typeElementModifiers = typeElement.getModifiers();
        if (typeElementModifiers.contains(Modifier.PRIVATE)) {
            needContinue = false;
        }

        if (typeElement.getKind() != ElementKind.CLASS) {
            needContinue = false;
        }

        return needContinue;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(FindView.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
