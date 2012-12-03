package by.jsendemail.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: a.evstratiev
 * Date: 11/23/12
 * Time: 3:51 AM
 */
public class FileScanner {

    public static Collection<File> getFilesByPath(String[] attachmentsPathes) {

        Collection<File> filesFullList = new ArrayList<File>();

        for (String path : attachmentsPathes) {
            String directoryName = FilenameUtils.getFullPath(path);
            String baseName = FilenameUtils.getBaseName(path);
            String extension = FilenameUtils.getExtension(path);

            String filesMask = "*.*";

            if (extension.length() != 0) {
                filesMask = baseName + "." + extension;
            }

            File directory = new File(directoryName);

            Collection<File> files = FileUtils.listFiles(directory, new WildcardFileFilter(filesMask), null);
            filesFullList.addAll(files);
        }

        return filesFullList;
    }

}
