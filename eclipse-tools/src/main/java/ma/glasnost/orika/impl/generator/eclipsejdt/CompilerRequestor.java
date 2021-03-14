/*
 * Orika - simpler, better and faster Java bean mapping
 *
 * Copyright (C) 2011-2013 Orika authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ma.glasnost.orika.impl.generator.eclipsejdt;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;

import java.util.HashMap;
import java.util.Map;

public class CompilerRequestor implements ICompilerRequestor {

    private Map<String, byte[]> compiledClassFiles;
    private IProblem[] problems;

    public CompilerRequestor() {
    	reset();
    }

	public Map<String, byte[]> getCompiledClassFiles() {
		return compiledClassFiles;
	}

	public IProblem[] getProblems() {
		return problems;
	}

	public void reset() {
		this.problems = null;
		this.compiledClassFiles = null;
		this.compiledClassFiles = new HashMap<>();
	}

	public void acceptResult(CompilationResult result) {
		boolean hasErrors = false;

		if (result.hasProblems()) {
			problems = result.getProblems();
		}

		if (!hasErrors) {

			ClassFile[] classFiles = result.getClassFiles();

			for (ClassFile classFile : classFiles) {
				char[][] compoundName = classFile.getCompoundName();
				StringBuilder className = new StringBuilder();
				String sep = "";

				for (char[] chars : compoundName) {
					className.append(sep);
					className.append(new String(chars));
					sep = ".";
				}

				byte[] bytes = classFile.getBytes();
				compiledClassFiles.put(className.toString(), bytes);
			}

		}
	}
}
