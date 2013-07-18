package net.minecraft.bootstrap;

import java.io.File;

public class Util
{
  public static final String APPLICATION_NAME = "minecraft";

  public static OS getPlatform()
  {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.contains("win")) return OS.WINDOWS;
    if (osName.contains("mac")) return OS.MACOS;
    if (osName.contains("linux")) return OS.LINUX;
    if (osName.contains("unix")) return OS.LINUX;
    return OS.UNKNOWN;
  }

  public static File getWorkingDirectory() {
    String userHome = System.getProperty("user.home", ".");
    File workingDirectory;
    switch (getPlatform().ordinal() + 1){
    case 1:
      String applicationData = System.getenv("APPDATA");
      String folder = applicationData != null ? applicationData : userHome;

      workingDirectory = new File(folder, ".minecraft/");
      break;
    case 2:
      workingDirectory = new File(userHome, "Library/Application Support/minecraft");
      break;
    case 3:
      workingDirectory = new File(userHome, ".minecraft/");
      break;
    case 4:
      workingDirectory = new File(userHome, ".minecraft/");
      break;
    default:
      workingDirectory = new File(userHome, "minecraft/");
    }
    return workingDirectory;
  }
 
  public static enum OS
  {
    WINDOWS, MACOS, SOLARIS, LINUX, UNKNOWN;
  }
}