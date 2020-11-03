/*
 * Copyright (C) 2012-2020 Gregory Hedlund
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
module ca.hedlund.jpraat {
	requires transitive com.sun.jna;
	
	exports ca.hedlund.jpraat;
	exports ca.hedlund.jpraat.annotations;
	exports ca.hedlund.jpraat.binding;
	exports ca.hedlund.jpraat.binding.fon;
	exports ca.hedlund.jpraat.binding.jna;
	exports ca.hedlund.jpraat.binding.melder;
	exports ca.hedlund.jpraat.binding.sys;
	exports ca.hedlund.jpraat.binding.stat;
	exports ca.hedlund.jpraat.exceptions;
}
