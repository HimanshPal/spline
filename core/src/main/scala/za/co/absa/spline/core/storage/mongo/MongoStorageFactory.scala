/*
 * Copyright 2017 Barclays Africa Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.spline.core.storage.mongo

import za.co.absa.spline.core.storage._

/**
  * The class represents a factory creating Mongo persistence layers for all main data lineage entities.
  *
  * @param dbUrl  An URL to Mongo host or cluster
  * @param dbName A name of Mongo database
  */
class MongoStorageFactory(dbUrl: String, dbName: String) extends DataStorageFactory {

  override def createDataLineageStorage(): DataLineageStorage = new MongoDataLineageStorage(dbUrl, dbName)

  override def createExecutionStorage(): ExecutionStorage = new MongoExecutionStorage(dbUrl, dbName)
}
