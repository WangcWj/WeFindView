package cn.wang.findview.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

class FindViewPlugins implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.plugins.each {

            if (it instanceof LibraryPlugin) {
                def variant = project.extensions.findByType(LibraryExtension.class).libraryVariants
                configureR2Generation(variant)
            }

            if (it instanceof AppPlugin) {
                def variant = project.extensions.findByType(AppExtension.class).applicationVariants
                configureR2Generation(variant)
            }
        }
    }

    private void configureR2Generation(Project project, DomainObjectSet<? extends BaseVariant> variants) {
        variants.all { BaseVariant variant->

           def rootpath =  project.buildDir.absolutePath
           def generationPath = rootpath+"generated/source/r2/${variant.getDirName()}"


        }

    }


}


