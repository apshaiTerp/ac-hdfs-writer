package com.ac.umkc.pbd;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * The goal of this program is pull the curated content of tweets by the venerable Donald Trump (@realDonaldTrump),
 * from the local file system into HDFS, preparatory to a word count program being run.
 * 
 * Technologies used here are:
 * <ul><li>Apache Hadoop Core and HDFS libraries for HDFS File writes</li>
 * <li>Maven is used as the build utility</li></ul>
 * 
 * This program expects two command line parameters, the first being the file path for the source OS
 * of the files in question.  The second is the file path under HDFS we want to write the file to.
 * This program should purge files in HDFS prior to copying data back in.
 * 
 * @author AC010168
 *
 */
public class BasicHadoopWriter {

  /** Local var to store the osFilePath root */
  private String osFilePath;
  /** Local var to store the hdfsFilePath we want to write our file to. */
  private String hdfsFilePath;
  
  public BasicHadoopWriter(String osFilePath, String hdfsFilePath) {
    this.osFilePath   = osFilePath;
    this.hdfsFilePath = hdfsFilePath;
  }
  
  /**
   * @param args list of command line arguments
   */
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println ("Expecting two input parameters.  ");
      return;
    }
    
    BasicHadoopWriter writer = new BasicHadoopWriter(args[0], args[1]);
    writer.execute();
  }
  
  /**
   * Helper method to drive the program execution.
   */
  public void execute() {
    //Now we begin the HDFS process
    try {
      Configuration hdfsConfiguration = new Configuration();
      FileSystem hdfs                 = FileSystem.get(hdfsConfiguration);
      
      Path localFile = new Path(osFilePath);
      Path hdfsFile  = new Path(hdfsFilePath);
      
      //If the HDFS version of the file already exists, purge it first
      if (hdfs.exists(hdfsFile)) {
        hdfs.delete(hdfsFile, false);
      }
      
      hdfs.copyFromLocalFile(false, true, localFile, hdfsFile);
    } catch (Throwable t) {
      System.err.println ("Something bad happened: " + t.getMessage());
      t.printStackTrace();
    }
  }
}
