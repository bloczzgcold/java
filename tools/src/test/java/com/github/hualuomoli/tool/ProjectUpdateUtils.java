package com.github.hualuomoli.tool;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class ProjectUpdateUtils {

  @Test
  public void test() throws IOException {
    String search = "com.github.hualuomoli.test";
    String replace = "com.github.hualuomoli.demo";

    String path = "demo";
    String output = "E:/p_1_2_3";

    String rootPath = this.getClass().getClassLoader().getResource(".").getPath();
    rootPath = rootPath.substring(0, rootPath.lastIndexOf("/tools/target"));
    String pathSearch = StringUtils.replace(search, ".", File.separator);
    String pathReplace = StringUtils.replace(replace, ".", File.separator);

    this.execute(search, replace, pathSearch, pathReplace, rootPath, new File(rootPath, path), output);

  }

  private void execute(String search, String replace, String pathSearch, String pathReplace, String rootPath, File path, String output) throws IOException {
    File[] files = path.listFiles();

    for (File file : files) {
      String name = file.getName();
      if (name.equals(".git") || name.equals(".settings") || name.equals(".classpath") || name.equals(".project") || name.equals("target")
          || name.equals("pom.xml.versionsBackup")) {
        continue;
      }
      if (file.isFile()) {
        // 文件名替换
        String absolutePath = file.getAbsolutePath();
        String relativePath = absolutePath.substring(rootPath.length());
        relativePath = StringUtils.replace(relativePath, pathSearch, pathReplace);

        // 内容替换
        String content = FileUtils.readFileToString(file);
        content = StringUtils.replace(content, search, replace);

        // 写内容
        File outFile = new File(output, relativePath);
        FileUtils.writeStringToFile(outFile, content);
      } else if (file.isDirectory()) {
        this.execute(search, replace, pathSearch, pathReplace, rootPath, file, output);
      } else {
        throw new RuntimeException();
      }
    }
  }

}
