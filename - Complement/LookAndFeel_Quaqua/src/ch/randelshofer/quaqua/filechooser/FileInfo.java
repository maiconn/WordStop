/*
 * @(#)FileInfo.java 
 *
 * Copyright (c) 2005-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */

package ch.randelshofer.quaqua.filechooser;

import java.io.*;
import javax.swing.*;
/**
 * Provides information about a File object. FileInfo uses a worker thread for
 * validating the information that it provides. The quality of the information
 * returned increases over time.
 * 
 * @author Werner Randelshofer
 * @version $Id: FileInfo.java 174 2010-01-09 16:06:52Z wrandelshofer $
 */
public interface FileInfo {
    /**
     * Returns the unresolved file object.
     */
    public File getFile();
    /**
     * Returns the resolved file object.
     */
    public File getResolvedFile();
    /**
     * Lazyily returns the resolved file object.
     * Returns null, if the file object has not been resolved yet.
     */
    public File lazyGetResolvedFile();
    
    /**
     * Returns true, if the file object is traversable.
     */
    public boolean isTraversable();
    /**
     * Returns true, if the file object is acceptable, i.e. selectable in
     * the JFileChooser.
     */
    public boolean isAcceptable();
    
    /**
     * Returns the (color) label of the file.
     * Returns -1 if the label has not (yet) been determined.
     */
    public int getFileLabel();
    
    /**
     * Returns the user name of the file.
     */
    public String getUserName();
    
    /**
     * Returns the icon of the file.
     * Returns a proxy icon if the real icon has not yet been fetched from the
     * file system.
     */
    public Icon getIcon();
    
    /**
     * Returns the length of the file.
     * Returns -1 if the length has not (yet) been determined.
     */
    public long getFileLength();
    
    /**
     * Returns the kind of the file.
     * Returns null if the kind has not (yet) been determined.
     */
    public String getFileKind();
    /**
     * Returns true if a worker thread is validating the information provided
     * by this file info object.
     */
    public boolean isValidating();
}
