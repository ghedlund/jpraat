/*
 * Copyright (C) 2012-2018 Gregory Hedlund
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.hedlund.jpraat.exceptions;

public class PraatException extends Exception {

	private static final long serialVersionUID = 376837834212187611L;

	public PraatException() {
		super();
	}

	public PraatException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PraatException(String message, Throwable cause) {
		super(message, cause);
	}

	public PraatException(String message) {
		super(message);
	}

	public PraatException(Throwable cause) {
		super(cause);
	}

	
}
