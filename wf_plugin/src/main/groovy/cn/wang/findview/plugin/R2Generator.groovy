package cn.wang.findview.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory


class R2Generator extends DefaultTask{

    @OutputDirectory
    File outputDir

    @InputFiles
    File rFile

    @Input
    def packagename

    @Input
    def className

    void brewJava(File outputDir,File rFile,String packagename,String className){

    }
}

