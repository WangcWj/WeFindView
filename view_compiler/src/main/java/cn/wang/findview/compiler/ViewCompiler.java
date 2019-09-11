package cn.wang.findview.compiler;

import com.google.auto.service.AutoService;

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
import javax.lang.model.element.TypeElement;
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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(FindView.class);
        if(null != elements && elements.size() > 0){
            mMessager.printMessage(Diagnostic.Kind.NOTE, "===========开始咯============");
            handleElements(elements);
        }
        return true;
    }

    private void handleElements(Set<? extends Element> elements) {


        for(Element element : elements){
            mMessager.printMessage(Diagnostic.Kind.NOTE, "-----第一个元素是： "+element);
            ElementKind kind = element.getKind();
            //确定我们只要字段类型的
            if(null == kind ||!kind.isField()){
                continue;
            }





            mMessager.printMessage(Diagnostic.Kind.NOTE, "-----第一个元素的ElementKind 是： "+kind);


        }

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
