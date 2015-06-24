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

import org.springframework.shell.event.ParseResult;

/**
 * Extension interface allowing command provider to be called when a shutdown hook occurs
 *
 * @author Jeferson Estevo
 */
public interface ShutdownHookListener extends CommandMarker {

    /**
     * Method called when a shutdown hook occurs while invoking the target command (described by {@link ParseResult})
     *
     * @param invocationContext target command context
     */
    void onShutdownHook(ParseResult invocationContext);

}
