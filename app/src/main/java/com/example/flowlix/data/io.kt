package com.example.flowlix.data

import android.content.Context
import android.util.Log
import java.io.*
import java.sql.Time


object io {

    var BASEPATH = "/data/data/com.example.flowlix/mydata"

    fun appendtofile(context: Context, path:String,fileName: String, data:String){

        val directory = File(BASEPATH+path)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        Log.d("ns","appending to path ${BASEPATH+path} and file $fileName")

        val file = File(BASEPATH +"$path" ,fileName)
        file.createNewFile()

        FileOutputStream(file, true).bufferedWriter().use { writer ->
            writer.appendLine(data)
        }
    }

    fun deletefile(context: Context,path: String,fileName: String){
        val file = File(BASEPATH +"$path" ,fileName)
        if(!file.delete()){
            throw Exception("File could not be deleted")
        }
    }

    fun read_last_line(path: String,fileName: String):String{
        val file = File(BASEPATH+"$path", fileName)
        val reader = BufferedReader(FileReader(file))
        var lastLine = ""
        var currentLine = reader.readLine()
        while (currentLine != null) {
            lastLine = currentLine
            currentLine = reader.readLine()
        }
        reader.close()
        return lastLine
    }

    fun read_file(context: Context, path: String ,fileName: String):String{

        val directory = File(path)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val fileString: String
        try {
            fileString = File(BASEPATH+"$path", fileName).readText(Charsets.UTF_8)
        } catch (ioException: IOException) {
            throw ioException
        }
        return fileString
    }

    fun check_if_file_exists(path:String, fileName: String): Boolean{
        var file = File(BASEPATH+"$path",fileName)
        return file.exists()
    }

    fun parseTimefromString(timestring:String):Time{
        val timevalues = timestring.split(":")
        return Time(timevalues[0].toInt(),timevalues[1].toInt(),timevalues[2].toInt())
    }

}