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

package za.co.absa.spline.core.salat

import java.net.URI
import java.util.UUID

import _root_.salat.grater
import com.mongodb.casbah.commons.Imports._
import org.scalatest.{FlatSpec, Matchers}

class BSONSalatContextSpec extends FlatSpec with Matchers {

  val aUUID: UUID = UUID fromString "7d46f047-da82-42fa-8e4b-4b085a210985"

  import BSONSalatContext._

  it should "serialize" in {
    val dbo = grater[Foo] asDBObject Foo(aUUID, new URI("http://example.com"))
    dbo get "uri" shouldEqual "http://example.com"
    dbo get "_id" shouldEqual aUUID
  }

  it should "deserialize" in {
    val foo = grater[Foo] asObject DBObject("_id" -> aUUID, "uri" -> "http://example.com")
    foo shouldEqual Foo(aUUID, new URI("http://example.com"))
  }

}

case class Foo(id: UUID, uri: URI)
