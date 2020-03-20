package cn.wang.findview.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeatureExtension
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.res.GenerateLibraryRFileTask
import com.android.build.gradle.internal.res.LinkApplicationAndroidResourcesTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.concurrent.atomic.AtomicBoolean

/**
 * 仿照ButterKnife写的插件，原理不是很懂。
 */
class FindViewPlugins implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.each {

            if(it instanceof FeaturePlugin){
                def variant = project.extensions.getByType(FeatureExtension.class).libraryVariants
                System.out.println("FeaturePlugin  ")
                configureGeneration(project, variant)
            }

            if (it instanceof LibraryPlugin) {
                def variant = project.extensions.findByType(LibraryExtension.class).libraryVariants
                System.out.println("走的是Library  ")
                configureGeneration(project, variant)
            }

            if (it instanceof AppPlugin) {
                def variant = project.extensions.findByType(AppExtension.class).applicationVariants
                System.out.println("走的是AppPlugin  ")
                configureGeneration(project, variant)
            }
        }
    }

    private void configureGeneration(Project project, DomainObjectSet<? extends BaseVariant> variants) {
        variants.all { BaseVariant variant ->

            def rootPath = project.buildDir.absolutePath
            def generationPath = rootPath + "${File.separator}generated${File.separator}source${File.separator}weFind${File.separator}${variant.getDirName()}"
            def outPutDir = new File(generationPath)
            def packageName = getPackageName(variant)
            def once = new AtomicBoolean()
            variant.outputs.all {
                BaseVariantOutput output ->
                    if (once.compareAndSet(false, true)) {
                        def res = output.processResources
                        def orgFile
                        if (res instanceof LinkApplicationAndroidResourcesTask) {
                            orgFile = res.getTextSymbolOutputFile()
                        } else if (res instanceof GenerateLibraryRFileTask) {
                            orgFile = res.getTextSymbolOutputFile()
                        }
                        if (orgFile) {
                            def varFile = project.files(orgFile).builtBy(res)
                            def generaTask = project.tasks.create("generate${variant.name.capitalize()}find", R3Generator.class){
                                if(it instanceof R3Generator){
                                    R3Generator generator = it
                                    generator.outputDir = outPutDir
                                    generator.rFile = varFile
                                    generator.packagename = packageName
                                    generator.className = "WeFindR"
                                }
                            }
                            variant.registerJavaGeneratingTask(generaTask, outPutDir)
                        }

                    }

            }
        }

    }

    private String getPackageName(BaseVariant variant) {
        def xmlParse = new XmlSlurper(false, false)
        List<File> list = new ArrayList<>()
        variant.sourceSets.each {
            list.add(it.manifestFile)
        }
        def result = xmlParse.parse(list.get(0))
        return result.getProperty("@package").toString()
    }


}


