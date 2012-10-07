package com.icode.generic.file;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.icode.generic.ICGenLineProcessor;
import com.icode.generic.app.ICAppFrame;
import com.icode.generic.base.ICGenTreeNode;
import com.icode.generic.task.ICTask;
import com.icode.generic.task.ICTaskTimed;

public class ICFileDirReader extends ICTaskTimed implements FileFilter, ICTask.EndListener {
	String directory;
	String fileMatchExpr;
	Matcher fileMatch;
	String fileReaderGroup;

	ICGenTreeNode lineProc;
	ICGenTreeNode fileReader;
	
	int activeReadingCount;

	protected long startTime = -1;
	long lastCheckTime;
	long patienceTime = -1;
	
	protected class FileReaderInfo {
		final String id;
		String name;
		public ICFileReader fileReader;
		long lastSize;
		long lastRead;

		public FileReaderInfo(String id, String name) {
			this.id = id;
			this.name = name;
			lastSize = -1;
		}
	}

	protected Map knownFiles = new HashMap();
	
	

	protected boolean isFileActive(File f) {
		return (f.lastModified() > startTime) && ((f.lastModified() > lastCheckTime) || !knownFiles.containsKey(f.getAbsolutePath()));
	}
	
	protected ICGenLineProcessor createProcessor(String name) {
		return (ICGenLineProcessor) ICAppFrame.getComponent(lineProc, ICGenLineProcessor.class);
	}
	
	protected boolean init() throws Exception {
		ICAppFrame.logInfoMsg("ICFileDirReader", "Start checking " + directory);
		return super.init();
	};


	protected void refreshFiles() throws Exception {
		if ( -1 == patienceTime ) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			patienceTime = cal.getTimeInMillis();
		}
		
		File rootDir = new File(directory);

		File[] files = rootDir.listFiles(this);

		synchronized (knownFiles) {
			Set missingFiles = new HashSet(knownFiles.keySet());
			Map currDirContentActive = new HashMap(knownFiles.size());
			boolean refreshFileInfo = false;

			if (null != files) {
				for (int i = files.length; i-- > 0;) {
					File f = files[i];
					String name = f.getAbsolutePath();
					missingFiles.remove(name);

					if (isFileActive(f)) {
						currDirContentActive.put(name, Boolean.TRUE);
						FileReaderInfo fi = (FileReaderInfo) knownFiles.get(name);
						refreshFileInfo |= ((null == fi) || (null == fi.fileReader));
					} else {
						currDirContentActive.put(name, Boolean.FALSE);
					}
				}
				
				for ( Iterator itMf = missingFiles.iterator(); itMf.hasNext(); ) {
					ICAppFrame.logInfoMsg("ICFileDirReader", "File " + itMf.next() + " was removed.");					
				}

				if (refreshFileInfo || (0 < missingFiles.size())) {
					ICFileIdentifier fid = new ICFileIdentifier.Unix();
					Map fileIDs = new HashMap(currDirContentActive);
					fid.getIDForFiles(fileIDs);
					
					activeReadingCount = 0;

					for (int fileIdx = 0; fileIdx < files.length; ++fileIdx) {
						File f = files[fileIdx];
						String name = f.getAbsolutePath();
						String fileID = (String) fileIDs.get(name);
						FileReaderInfo currFI = null;

						for (Iterator itKFs = knownFiles.entrySet().iterator(); (null == currFI) && itKFs.hasNext();) {
							Map.Entry e2 = (Map.Entry) itKFs.next();
							FileReaderInfo fi = (FileReaderInfo) e2.getValue();
							if (fi.id.equals(fileID)) {
								currFI = fi;
							}
						}

						if (null == currFI) {
							currFI = new FileReaderInfo(fileID, name);
						} else {
							if (!currFI.name.equals(name)) {
								ICAppFrame.logInfoMsg("ICFileDirReader", "File " + fileID + " was renamed from " + currFI.name + " to " + name);
								if ( null != currFI.fileReader ) {
									currFI.fileReader.fileName = name;
								}
								currFI.name = name;
							}
						}

						if ((null == currFI.fileReader) && ((Boolean) currDirContentActive.get(name)).booleanValue()) {
							ICAppFrame.logInfoMsg("ICFileDirReader", "Starting reader for " + fileID + " : " + name);

							ICFileReader fr = (ICFileReader) ICAppFrame.getComponent(fileReader, ICFileReader.class);
							fr.fileName = name;
							fr.setOwner(this);
							fr.setProcessor(createProcessor(name));
							if ( f.lastModified() < patienceTime ) {
								fr.setRetry(0, WAIT_NONE);
							}
							currFI.fileReader = fr;

							if (-1 == currFI.lastSize) {
								ICAppFrame.logInfoMsg("ICFileDirReader", name + " started as a new file");
							} else {
								fr.lastFilePos = currFI.lastSize;
								ICAppFrame.logInfoMsg("ICFileDirReader", name + " confinues from position " + fr.lastFilePos);
							}

							ICAppFrame.getTaskManager().addTask(fr);
							if ( null == fileReaderGroup ) {
								fileReaderGroup = fr.getGroupName();
								ICAppFrame.getTaskManager().startGroup(fileReaderGroup, this, WAIT_FOREVER);
							}
						}

						fileIDs.put(name, currFI);
						
						if ( null != currFI.fileReader) {
							++activeReadingCount;
						}
					}

					knownFiles.clear();
					knownFiles.putAll(fileIDs);
				} else {
//					ICAppFrame.log(EVENT_LEVEL_DEBUG, "ICFileDirReader", directory + ": no change", null);
				}
			}
		}
		
		lastCheckTime = System.currentTimeMillis();
	}

	protected boolean doRepeatedTask() throws Exception {
		setMessage("Refreshing files...");
		refreshFiles();
		setMessage("Currently reading " + activeReadingCount + " files");
		return true;
	}

	public void loadDataFrom(ICGenTreeNode config, Object hint) throws Exception {
		super.loadDataFrom(config, hint);
		directory = (String) config.getMandatory("directory");
		fileMatchExpr = (String) config.getMandatory("fileMatchExpr");
		fileReader = config.getChild("fileReader");
		lineProc = config.getChild("lineProc");
		
		File rootDir = new File(directory);
		if (!rootDir.exists() || !rootDir.isDirectory()) {
			throw new Exception("invalid logDirectory specified: " + directory);
		}

		fileMatch = Pattern.compile(fileMatchExpr).matcher("");

	}

	public boolean accept(File file) {
		if ( file.isDirectory() ) {
			return false;
		}
		if ( 0 == file.length() ) {
			return false;
		}
		String name = file.getName();
		if (fileMatch != null) {
			fileMatch.reset(name);
			return fileMatch.matches();
		} else {
			return false;
		}
	}

	public void taskEnded(ICTask task) {
		synchronized (knownFiles) {
			activeReadingCount = 0;
			for (Iterator itKFs = knownFiles.entrySet().iterator(); itKFs.hasNext();) {
				Map.Entry eKFs = (Map.Entry) itKFs.next();
				FileReaderInfo fi = (FileReaderInfo) eKFs.getValue();
				ICFileReader fr = fi.fileReader;

				if (fr == task) {
					fi.lastSize = fr.lastFilePos;
					ICAppFrame.logInfoMsg("ICFileDirReader", fr.fileName + " finished reading " + fr.getLineCount() + " lines.");
					fi.fileReader = null;
				}
				
				if ( null != fi.fileReader) {
					++activeReadingCount;
				}

			}
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

}
