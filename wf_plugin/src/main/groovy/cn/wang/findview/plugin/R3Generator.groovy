package cn.wang.findview.plugin

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import javax.lang.model.element.Modifier

/**
 * 生成代码的自定义任务，任务被执行的时候，会调用generator方法。仿照ButterKnife的插件写的。
 */
class R3Generator extends DefaultTask {

    def SUPPORT = ["anim", "array", "attr", "bool", "color", "dimen",
                   "drawable", "id", "integer", "layout", "menu", "plurals", "string", "style", "styleable"]

    @OutputDirectory
    File outputDir

    @InputFiles
    FileCollection rFile

    @Input
    def packagename

    @Input
    def className

    def resourceTypes = new HashMap<String, TypeSpec.Builder>()

    // Invoked by Gradle.
    @SuppressWarnings("unused")
    @TaskAction
    void generator() {
        System.println("=======执行到这里======= " + rFile)
        if (null != rFile) {
            write(outputDir, rFile.getSingleFile(), packagename, className)
        }
    }

    void write(File outputDir, File rFile, String packageName, String className) {
        System.println("=======packageName是======= " + packageName)
        rFile.eachLine { String str ->
            handleLineStr(str)
        }
        def generaClass = TypeSpec.classBuilder(className).addModifiers(Modifier.FINAL, Modifier.PUBLIC)
        resourceTypes.each {
            generaClass.addType(it.value.build())
        }
        JavaFile fileBuild = JavaFile.builder(packageName, generaClass.build())
                .addFileComment("Generated code from WeFind gradle plugin. Do not modify!")
                .build()
        fileBuild.writeTo(outputDir)
        System.println("=======最后的集合是======= " + resourceTypes)

    }

    /**
     * 读取R文件中的没一行之后，需要分割字符串。
     * @param str
     */
    void handleLineStr(String str) {
        // 类似于这样的的: int attr actionModeSelectAllDrawable 0x7f020018

        //分割字符串
        def values = str.split(' ')
        //正常的话应该会被分割为四个元素.
        if (values.size() < 4) {
            return
        }
        //第一个元素是属性.
        def javaType = values[0]
        if ("int" != javaType) {
            return
        }
        //第二元素是属性的所属类型.
        def valueType = values[1]
        if ("id" != valueType) {
            return
        }
        //获取属性值的名字.
        def key = values[2]
        //获取属性值.
        def value = values[3]
        //使用javaPoet生成java文件.
        //构造属性元素.
        def builder = FieldSpec.builder(int.class, key)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer(value)
        //构造类元素.
        def typeSpec = resourceTypes.get(valueType,
                TypeSpec.classBuilder(valueType)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
        )
        //存入Map中.
        typeSpec.addField(builder.build())
    }
}

