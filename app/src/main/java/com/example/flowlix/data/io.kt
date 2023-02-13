package com.example.flowlix.data

import android.content.Context
import android.util.Log
import java.io.*
import java.sql.Time


/**
 * Object containing various I/O operations used in the app.
 *
 * @property BASEPATH The base file path for accessing the data stored in the app.
 */
object io {

    var BASEPATH = "/data/data/com.example.flowlix/mydata"


    /**
     * Appends the given [data] to the file at the specified [path] and [fileName].
     *
     * @param context The context used to access the file system.
     * @param path The path to the directory containing the file.
     * @param fileName The name of the file to which the data is being appended.
     * @param data The data to be appended to the file.
     */
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

    /**
     * Deletes the file at the specified [path] and [fileName].
     *
     * @param context The context used to access the file system.
     * @param path The path to the directory containing the file.
     * @param fileName The name of the file to be deleted.
     *
     * @throws Exception If the file could not be deleted.
     */
    fun deletefile(context: Context,path: String,fileName: String){
        val file = File(BASEPATH +"$path" ,fileName)
        if(!file.delete()){
            throw Exception("File could not be deleted")
        }
    }

    /**
     * Reads the last line of the file at the specified [path] and [fileName].
     *
     * @param path The path to the directory containing the file.
     * @param fileName The name of the file to be read.
     *
     * @return The last line of the file as a string.
     */
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

    /**
     * Checks if the file at the specified [path] and [fileName] exists.
     *
     * @param path The path to the directory containing the file.
     * @param fileName The name of the file to be checked.
     *
     * @return True if the file exists, false otherwise.
     */
    fun check_if_file_exists(path:String, fileName: String): Boolean{
        var file = File(BASEPATH+"$path",fileName)
        return file.exists()
    }

    /**
     * Parses a [Time] object from the given [timestring].
     *
     * @param timestring The string representation of the time to be parsed, in the format "HH:mm:ss".
     *
     * @return The parsed [Time] object.
     */
    fun parseTimefromString(timestring:String):Time{
        val timevalues = timestring.split(":")
        return Time(timevalues[0].toInt(),timevalues[1].toInt(),timevalues[2].toInt())
    }

}