/*
 * Copyright 2011-2012 the original author or authors.
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
package org.springframework.shell.core;

import java.lang.reflect.Proxy;
import java.util.logging.Logger;

import org.springframework.aop.support.AopUtils;
import org.springframework.shell.event.ParseResult;
import org.springframework.shell.support.logging.HandlerUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * Simple execution strategy for invoking a target method.
 * Supports pre/post processing to allow {@link CommandMarker}s for aop-like behavior (
 * typically used for controlling stateful objects).
 *
 * @author Mark Pollack
 * @author Costin Leau
 */
public class SimpleExecutionStrategy implements ExecutionStrategy {

	private static final Logger logger = HandlerUtils.getLogger(SimpleExecutionStrategy.class);

	private final Class<?> mutex = SimpleExecutionStrategy.class;

	public Object execute(ParseResult parseResult) throws RuntimeException {
		Assert.notNull(parseResult, "Parse result required");
		synchronized (mutex) {
			Assert.isTrue(isReadyForCommands(), "SimpleExecutionStrategy not yet ready for commands");
			Object target = parseResult.getInstance();
			try {
				JLineShell.currentCommand = parseResult;
				if (target instanceof ExecutionProcessor) {
					ExecutionProcessor processor = ((ExecutionProcessor)target);
					parseResult = processor.beforeInvocation(parseResult);
					try {
					    	Object result = invoke(parseResult);
					    	processor.afterReturningInvocation(parseResult, result);
					    	return result;
					}
					catch (Throwable th) {
					    	processor.afterThrowingInvocation(parseResult, th);
					    	return handleThrowable(th);
					}
				}
			 	else {
					return invoke(parseResult);
				}
			} finally {
				JLineShell.currentCommand = null;
			}
		}
	}

	private Object invoke(ParseResult parseResult) {
		try {
		    if (AopUtils.isJdkDynamicProxy(parseResult.getInstance())) {
			return Proxy.getInvocationHandler(parseResult.getInstance()).invoke(parseResult.getInstance(),
				parseResult.getMethod(), parseResult.getArguments());
		    } else {
			return ReflectionUtils.invokeMethod(parseResult.getMethod(), parseResult.getInstance(),
				parseResult.getArguments());
		    }
		} catch (Throwable th) {
			logger.severe("Command failed " + th);
			return handleThrowable(th);
		}
	}

	private Object handleThrowable(Throwable th) {
		if (th instanceof Error) {
			throw ((Error) th);
		}
		if (th instanceof RuntimeException) {
			throw ((RuntimeException) th);
		}
		throw new RuntimeException(th);
	}

	public boolean isReadyForCommands() {
		return true;
	}

	public void terminate() {
		// do nothing
	}

}
