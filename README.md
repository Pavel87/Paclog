
#PacLog Library:

Android library wrapping the logging functionality. Instead of writing bunch of code for writing a custom messages in specific log file you can now simply import the Paclog library from JCenter and start using it.
Everything has been designed to simplify the work so you need only few lines of code in order to start writing messages in your custom file.



#Setup Gradle dependency in your app module like this:

	dependencies {

   		 compile 'com.pacmac.paclog:pac-log:1.0.0'

	}


#Initialize Paclog:
In your Activity create a PacLog object and initialise it either with context only or adding file size and file name. There also other parameters which can be set once the PacLog object exist.

        PacLog pacLog = PacLog.setUP(getApplicationContext());

        PacLog pacLog = PacLog.setUP(getApplicationContext(), 1024*5, "paclog");


#In order to write the file it is necessary to call following method:
  
    writePacLog(String message) 


#List of available methods:

    void exoportInternal()

    String getAbsoluthPath()

    String getFileExtension()

    String getFileName() 

    long getFileSize()

    int getStorageOption()

    int getVersion()

    boolean isExported()

    PacLog setFileName(String fileName) 

    PacLog setFileSize(long fileSize) 

    PacLog setFileExtension(String fileExtension)

    PacLog setAbsoluthPath(String absoluthPath)

    PacLog setStorageOption(int storageOption)

    String toString()

    void wipeLogs()

    void writePacLog(String message) 

     
