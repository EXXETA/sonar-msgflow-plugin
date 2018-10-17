/*
 * Sonar Message Flow Plugin
 * Copyright (C) 2015 Hendrik Scholz and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;

import com.exxeta.iss.sonar.msgflow.MessageFlowLanguage;
import com.google.common.base.Charsets;

public abstract class AbstractSensorTest {
	private File baseDir = new File("src/test/resources");
	private SensorContextTester context = SensorContextTester.create(baseDir);
	
	protected void inputFile(String relativePath) {
		DefaultInputFile inputFile =  new TestInputFileBuilder("moduleKey", relativePath)
				.setModuleBaseDir(baseDir.toPath())
				.setType(Type.MAIN)
				.setLanguage(MessageFlowLanguage.KEY)
				.setCharset(StandardCharsets.UTF_8)
				.build();

		context.fileSystem().add(inputFile);

		try {
			inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()), Charsets.UTF_8, inputFile.absolutePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SensorContextTester getContext()
	{
		return context;
	}
}