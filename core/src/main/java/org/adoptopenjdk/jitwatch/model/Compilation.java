/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/AdoptOpenJDK/jitwatch/blob/master/LICENSE-BSD
 * Instructions: https://github.com/AdoptOpenJDK/jitwatch/wiki
 */
package org.adoptopenjdk.jitwatch.model;

import java.util.HashMap;
import java.util.Map;

import org.adoptopenjdk.jitwatch.model.assembly.AssemblyMethod;
import org.adoptopenjdk.jitwatch.util.ParseUtil;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_COMPILE_ID;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_ADDRESS;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_COMPILER;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_NMSIZE;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_COMPILE_KIND;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.ATTR_LEVEL;
import static org.adoptopenjdk.jitwatch.core.JITWatchConstants.C2N;

public class Compilation
{
	private Tag tagTaskQueued;

	private Tag tagNMethod;

	private Task tagTask;

	private Tag tagTaskDone;

	private AssemblyMethod assembly;

	private long compileTime;

	private String compileID;

	private long queuedStamp;

	private long compiledStamp;

	private String nativeAddress;

	private int index;
	
	//TODO mem saving: remove attributes from strings / maps once copied into above local fields

	public Compilation(int index)
	{
		this.index = index;
	}

	public String getCompileID()
	{
		return compileID;
	}

	public String getNativeAddress()
	{
		return nativeAddress;
	}

	public void setNativeAddress(String nativeAddress)
	{
		this.nativeAddress = nativeAddress;
	}

	public AssemblyMethod getAssembly()
	{
		return assembly;
	}

	public void setAssembly(AssemblyMethod assembly)
	{
		this.assembly = assembly;
	}

	public Map<String, String> getQueuedAttributes()
	{
		Map<String, String> result = null;

		if (tagTaskQueued != null)
		{
			result = tagTaskQueued.getAttributes();
		}
		else
		{
			result = new HashMap<>();
		}

		return result;
	}

	public String getQueuedAttribute(String key)
	{
		return getQueuedAttributes().get(key);
	}

	public Map<String, String> getCompiledAttributes()
	{
		return tagNMethod.getAttributes();
	}

	public String getCompiledAttribute(String key)
	{
		return getCompiledAttributes().get(key);
	}

	public void setTagTaskQueued(Tag tagTaskQueued)
	{
		this.tagTaskQueued = tagTaskQueued;

		this.compileID = tagTaskQueued.getAttributes().get(ATTR_COMPILE_ID);

		queuedStamp = ParseUtil.getStamp(tagTaskQueued.getAttributes());
	}

	public void setTagNMethod(Tag tagNMethod)
	{
		this.tagNMethod = tagNMethod;

		Map<String, String> attrs = tagNMethod.getAttributes();

		this.nativeAddress = attrs.get(ATTR_ADDRESS);

		String compileKind = attrs.get(ATTR_COMPILE_KIND);

		compiledStamp = ParseUtil.getStamp(attrs);

		if (!C2N.equals(compileKind))
		{
			if (queuedStamp != 0 && compiledStamp != 0)
			{
				compileTime = compiledStamp - queuedStamp;
			}
		}
	}

	public void setTagTask(Task tagTask)
	{
		this.tagTask = tagTask;
	}

	public Tag getTagTaskQueued()
	{
		return tagTaskQueued;
	}

	public Tag getTagNMethod()
	{
		return tagNMethod;
	}

	public Task getTagTask()
	{
		return tagTask;
	}

	public Tag getTagTaskDone()
	{
		return tagTaskDone;
	}

	public void setTagTaskDone(Tag tagTaskDone)
	{
		this.tagTaskDone = tagTaskDone;
	}

	public long getCompileTime()
	{
		return compileTime;
	}

	public int getIndex()
	{
		return index;
	}

	public int getNativeSize()
	{
		int result = 0;

		if (tagTaskDone != null)
		{
			result = Integer.parseInt(tagTaskDone.getAttributes().get(ATTR_NMSIZE));
		}

		return result;
	}

	public long getQueuedStamp()
	{
		return queuedStamp;
	}

	public long getCompiledStamp()
	{
		return compiledStamp;
	}

	public String getSignature()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("#").append(index + 1);

		if (tagNMethod != null)
		{
			Map<String, String> tagAttributes = tagNMethod.getAttributes();

			String level = tagAttributes.get(ATTR_LEVEL);
			String compiler = tagAttributes.get(ATTR_COMPILER);
			String compileKind = tagAttributes.get(ATTR_COMPILE_KIND);

			builder.append("  (");

			if (compiler != null)
			{
				builder.append(compiler);
			}

			if (compileKind != null)
			{
				if (compiler != null)
				{
					builder.append(" / ");
				}

				builder.append(compileKind.toUpperCase());
			}

			if (level != null)
			{
				builder.append(" / Level ").append(level);
			}

			builder.append(")");
		}

		return builder.toString();
	}

	public String getCompiler()
	{
		String result = null;

		if (tagNMethod != null)
		{
			StringBuilder builder = new StringBuilder();

			Map<String, String> tagAttributes = tagNMethod.getAttributes();

			String compiler = tagAttributes.get(ATTR_COMPILER);
			String compileKind = tagAttributes.get(ATTR_COMPILE_KIND);

			if (compiler != null)
			{
				builder.append(compiler);
			}

			if (compileKind != null)
			{
				if (compiler != null)
				{
					builder.append(" ");
				}

				builder.append(compileKind.toUpperCase());
			}

			result = builder.toString();
		}

		return result;
	}

	public String getLevel()
	{
		String result = null;

		if (tagNMethod != null)
		{
			StringBuilder builder = new StringBuilder();

			Map<String, String> tagAttributes = tagNMethod.getAttributes();

			String level = tagAttributes.get(ATTR_LEVEL);

			if (level != null)
			{
				builder.append("Level ").append(level);
			}

			result = builder.toString();
		}

		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		if (tagTaskQueued != null)
		{
			builder.append(tagTaskQueued).append("\n");
		}

		if (tagNMethod != null)
		{
			builder.append(tagNMethod).append("\n");
		}

		if (tagTask != null)
		{
			builder.append(tagTask).append("\n");
		}

		return builder.toString();
	}
}