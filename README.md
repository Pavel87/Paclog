
#PacLog Library:

Android library wrapping the logging functionality. Instead of writing bunch of code for writing a custom messages in specific log file you can now simply import the Paclog library from JCenter and start using it.
Everything has been designed to simplify the work so you need only few lines of code in order to start writing messages in your custom file.



#Setup Gradle dependency in your app module:

	dependencies {

   		 compile 'com.pacmac.paclog:pac-log:1.0.0'

	}


#Initialize Paclog:
In your Activity create a PacLog object and initialise it either with context only or adding file size and file name. There also other parameters which can be set once the PacLog object exist.

        PacLog pacLog = PacLog.setUP(getApplicationContext());

        PacLog pacLog = PacLog.setUP(getApplicationContext(), 1024*5, "paclog");


#In order to write the file it is necessary to call following method:
  
    writePacLog(String message) 

- Method writes the message in the specified log file in format Month/Year Hours:Minutes:Seconds.Miliseconds Message.


#List of available methods:

    void exoportInternal()
Exports the log if log file from internal memory to external storage.


    String getAbsoluthPath()

Returns absoluth path to log file 


    String getFileExtension()

Return file extension


    String getFileName() 

Returns file name


    long getFileSize()

Returns file size


    int getStorageOption()

Returns storage option


    int getVersion()

Returns lib version


    PacLog setFileName(String fileName) 

Set file name


    PacLog setFileSize(long fileSize) 

Set file size in bytes


    PacLog setFileExtension(String fileExtension)

Set log file extension (default: "txt")


    PacLog setAbsoluthPath(String absoluthPath)

Set storage path (i.e. "/storage/sdcar0/logs/")


    PacLog setStorageOption(int storageOption)

Set storage to internal/external or custom path. If custom path is set, then it is necessary to 


    setAbsoluthPath(String path)

Possible values INTERNAL_STORAGE, SECONDARY_STORAGE (default), CUSTOM_STORAGE


    String toString()

Returns last message which was stored in log file


    void wipeLogs()

Deletes all stored logs including backup file


    void writePacLog(String message) 

Method writes the message in the specified log file in format Month/Year Hours:Minutes:Seconds.Miliseconds Message.


#PacLog Listener:
PaclogListener can be used within user mode in case you want to retrieve notification when log file is exported from internal to external memory:

    void onExport(boolean isExported);

     
