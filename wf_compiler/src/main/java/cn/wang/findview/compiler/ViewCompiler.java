package cn.wang.findview.compiler;

import com.google.auto.service.AutoService;

import java.util.Iterator;
import java.util.LinkedHashSet;
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

    static final String VIEW_TYPE = "android.view.View";

    private Messager mMessager;
    private Elements elementUtils;
    private Types typeUtils;

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
        TypeMirror viewTypeMirror = elementUtils.getTypeElement(VIEW_TYPE).asType();

        mMessager.printMessage(Diagnostic.Kind.NOTE, "-----元素是： " + element);
        ElementKind kind = element.getKind();
        //确定我们只要字段类型的
        if (!kind.isField()) {
            return;
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "是个属性 " + element);
        //获取该属性所属类元素.
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        //查看注解的属性是否能访问到.
        if(!canGeneratedCode(element,enclosingElement)){
            return;
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "也能访问到 " + element);

        TypeMirror typeMirror = element.asType();
        if(typeMirror.getKind() == TypeKind.TYPEVAR){
            //是一个属性元素.
            TypeVariable tv = (TypeVariable) typeMirror;
            //获取最上层的元素
            typeMirror = tv.getUpperBound();
        }
        //属性不是View的话就返回.
        if(!typeUtils.isSubtype(typeMirror,viewTypeMirror)){
            return;
        }



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
    private boolean canGeneratedCode(Element element,TypeElement typeElement) {
        boolean needContinue = true;

        Set<Modifier> modifiers = element.getModifiers();
        if(modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)){
            needContinue = false;
        }

        Set<Modifier> typeElementModifiers = typeElement.getModifiers();
        if(typeElementModifiers.contains(Modifier.PRIVATE)){
            needContinue = false;
        }

        if(typeElement.getKind() != ElementKind.CLASS){
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
