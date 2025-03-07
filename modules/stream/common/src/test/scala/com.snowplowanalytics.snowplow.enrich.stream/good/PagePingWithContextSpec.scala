/*
 * Copyright (c) 2013-2022 Snowplow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0, and
 * you may not use this file except in compliance with the Apache License
 * Version 2.0.  You may obtain a copy of the Apache License Version 2.0 at
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Apache License Version 2.0 is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the Apache License Version 2.0 for the specific language
 * governing permissions and limitations there under.
 */
package com.snowplowanalytics.snowplow.enrich.stream
package good

import org.apache.commons.codec.binary.Base64
import org.specs2.mutable.Specification
import org.specs2.execute.Result

import SpecHelpers._

object PagePingWithContextSpec {

  val raw =
    "CgABAAABQ/RDKr8LABQAAAAQc3NjLTAuMS4wLXN0ZG91dAsAHgAAAAVVVEYtOAsAKAAAAAgxMC4wLjIuMgwAKQgAAQAAAAEIAAIAAAABCwADAAAC/WU9cHAmcGFnZT1Bc3luY2hyb25vdXMrd2Vic2l0ZS93ZWJhcHArZXhhbXBsZXMrZm9yK3Nub3dwbG93LmpzJnBwX21peD0wJnBwX21heD0wJnBwX21peT0wJnBwX21heT0wJmN4PWV5SnpZMmhsYldFaU9pSnBaMngxT21OdmJTNXpibTkzY0d4dmQyRnVZV3g1ZEdsamN5NXpibTkzY0d4dmR5OWpiMjUwWlhoMGN5OXFjMjl1YzJOb1pXMWhMekV0TUMwd0lpd2laR0YwWVNJNlczc2ljMk5vWlcxaElqb2lhV2RzZFRwamIyMHVjMjV2ZDNCc2IzZGhibUZzZVhScFkzTXVjMjV2ZDNCc2IzY3ZkMlZpWDNCaFoyVXZhbk52Ym5OamFHVnRZUzh4TFRBdE1DSXNJbVJoZEdFaU9uc2lhV1FpT2lKaU1EVmlNekZqTXkwNE1XRmpMVFJoWmpVdE9USmtNUzB4TVRNeE16TTVOamcyTlRVaWZYMWRmUSZkdG09MTM5MTM3MjQ3OTMyOSZ0aWQ9NTc2NjY4JnZwPTE2ODB4NDE1JmRzPTE2ODB4NDE1JnZpZD0yNSZkdWlkPTNjMTc1NzU0NGUzOWJjYTQmcD13ZWImdHY9anMtMC4xMy4xJmZwPTE4MDQ5NTQ3OTAmYWlkPUNGZTIzYSZsYW5nPWVuLVVTJmNzPVVURi04JnR6PUV1cm9wZS9Mb25kb24mdWlkPWFsZXgrMTIzJmZfcGRmPTAmZl9xdD0xJmZfcmVhbHA9MCZmX3dtYT0wJmZfZGlyPTAmZl9mbGE9MSZmX2phdmE9MCZmX2dlYXJzPTAmZl9hZz0wJnJlcz0xOTIweDEwODAmY2Q9MjQmY29va2llPTEmdXJsPWZpbGU6Ly9maWxlOi8vL1VzZXJzL2FsZXgvRGV2ZWxvcG1lbnQvZGV2LWVudmlyb25tZW50L2RlbW8vMS10cmFja2VyL2V2ZW50cy5odG1sL292ZXJyaWRkZW4tdXJsLwALAC0AAAAJbG9jYWxob3N0CwAyAAAAUU1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwLjk7IHJ2OjI2LjApIEdlY2tvLzIwMTAwMTAxIEZpcmVmb3gvMjYuMA8ARgsAAAAHAAAAFkNvbm5lY3Rpb246IGtlZXAtYWxpdmUAAAJwQ29va2llOiBfX3V0bWE9MTExODcyMjgxLjg3ODA4NDQ4Ny4xMzkwMjM3MTA3LjEzOTA5MzE1MjEuMTM5MTExMDU4Mi43OyBfX3V0bXo9MTExODcyMjgxLjEzOTAyMzcxMDcuMS4xLnV0bWNzcj0oZGlyZWN0KXx1dG1jY249KGRpcmVjdCl8dXRtY21kPShub25lKTsgX3NwX2lkLjFmZmY9Yjg5YTZmYTYzMWVlZmFjMi4xMzkwMjM3MTA3LjcuMTM5MTExMTgxOS4xMzkwOTMxNTQ1OyBoYmxpZD1DUGpqdWh2RjA1emt0UDdKN001Vm8zTklHUExKeTFTRjsgb2xmc2s9b2xmc2s1NjI5MjM2MzU2MTc1NTQ7IHNwPTc1YTEzNTgzLTVjOTktNDBlMy04MWZjLTU0MTA4NGRmYzc4NDsgd2NzaWQ9S1JoaGs0SEVMcDJBaXBxTDdNNVZvbkNQT1B5QW5GMUo7IF9va2x2PTEzOTExMTE3NzkzMjglMkNLUmhoazRIRUxwMkFpcHFMN001Vm9uQ1BPUHlBbkYxSjsgX191dG1jPTExMTg3MjI4MTsgX29rYms9Y2Q0JTNEdHJ1ZSUyQ3ZpNSUzRDAlMkN2aTQlM0QxMzkxMTEwNTg1NDkwJTJDdmkzJTNEYWN0aXZlJTJDdmkyJTNEZmFsc2UlMkN2aTElM0RmYWxzZSUyQ2NkOCUzRGNoYXQlMkNjZDYlM0QwJTJDY2Q1JTNEYXdheSUyQ2NkMyUzRGZhbHNlJTJDY2QyJTNEMCUyQ2NkMSUzRDAlMkM7IF9vaz05NzUyLTUwMy0xMC01MjI3AAAAHkFjY2VwdC1FbmNvZGluZzogZ3ppcCwgZGVmbGF0ZQAAABpBY2NlcHQtTGFuZ3VhZ2U6IGVuLVVTLCBlbgAAACtBY2NlcHQ6IGltYWdlL3BuZywgaW1hZ2UvKjtxPTAuOCwgKi8qO3E9MC41AAAAXVVzZXItQWdlbnQ6IE1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwLjk7IHJ2OjI2LjApIEdlY2tvLzIwMTAwMTAxIEZpcmVmb3gvMjYuMAAAABRIb3N0OiBsb2NhbGhvc3Q6NDAwMQsAUAAAACQ3NWExMzU4My01Yzk5LTQwZTMtODFmYy01NDEwODRkZmM3ODQA"

  val expected = List[StringOrRegex](
    "CFe23a",
    "web",
    TimestampRegex,
    "2014-02-02 20:21:19.167",
    "2014-02-02 20:21:19.329",
    "page_ping",
    Uuid4Regexp,
    "576668",
    "", // No tracker name
    "js-0.13.1",
    "ssc-0.1.0-stdout",
    v_etl,
    "d1a21f2589511b4ed04ee297d88d950efb2612dc",
    "850474a1f035479d332a5c2d2ad6fe4d07a3f722",
    "1804954790",
    "3c1757544e39bca4",
    "25",
    "75a13583-5c99-40e3-81fc-541084dfc784",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "file://file:///Users/alex/Development/dev-environment/demo/1-tracker/events.html/overridden-url/",
    "Asynchronous website/webapp examples for snowplow.js",
    "",
    "file",
    "file",
    "80",
    "///Users/alex/Development/dev-environment/demo/1-tracker/events.html/overridden-url/",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    """{"schema":"iglu:com.snowplowanalytics.snowplow/contexts/jsonschema/1-0-0","data":[{"schema":"iglu:com.snowplowanalytics.snowplow/web_page/jsonschema/1-0-0","data":{"id":"b05b31c3-81ac-4af5-92d1-113133968655"}}]}""",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "0",
    "0",
    "0",
    "0",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0",
    "Firefox 26",
    "Firefox",
    "26.0",
    "Browser",
    "GECKO",
    "en-US",
    "0",
    "1",
    "0",
    "0",
    "1",
    "0",
    "0",
    "0",
    "0",
    "1",
    "24",
    "1680",
    "415",
    "Mac OS X",
    "Mac OS X",
    "Apple Inc.",
    "Europe/London",
    "Computer",
    "0",
    "1920",
    "1080",
    "UTF-8",
    "1680",
    "415",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "2014-02-02 20:21:19.167",
    "com.snowplowanalytics.snowplow",
    "page_ping",
    "jsonschema",
    "1-0-0",
    "",
    ""
  )
  val pii = List[StringOrRegex](
    "CFe23a",
    "srv",
    TimestampRegex,
    "2014-02-02 20:21:19.167",
    "",
    "pii_transformation",
    Uuid4Regexp, // Regexp match
    "",
    "",
    "",
    "",
    v_etl,
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    ContextWithUuid4Regexp,
    "",
    "",
    "",
    "",
    "",
    """{"schema":"iglu:com.snowplowanalytics.snowplow/unstruct_event/jsonschema/1-0-0","data":{"schema":"iglu:com.snowplowanalytics.snowplow/pii_transformation/jsonschema/1-0-0","data":{"pii":{"pojo":[{"fieldName":"user_ipaddress","originalValue":"10.0.2.x","modifiedValue":"850474a1f035479d332a5c2d2ad6fe4d07a3f722"},{"fieldName":"user_id","originalValue":"alex 123","modifiedValue":"d1a21f2589511b4ed04ee297d88d950efb2612dc"}]},"strategy":{"pseudonymize":{"hashFunction":"SHA-1"}}}}}""",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    TimestampRegex,
    "com.snowplowanalytics.snowplow",
    "pii_transformation",
    "jsonschema",
    "1-0-0",
    "",
    TimestampRegex
  )

}

class PagePingWithContextSpec extends Specification {

  "Stream Enrich" should {

    "enrich a valid page ping with context" in {

      val rawEvent = Base64.decodeBase64(PagePingWithContextSpec.raw)

      val enrichedEvent = TestSource.enrichEvents(rawEvent)(0)
      enrichedEvent.isValid must beTrue

      // "-1" prevents empty strings from being discarded from the end of the array
      val fields = enrichedEvent.toOption.get._1.split("\t", -1)
      val piiFields = enrichedEvent.toOption.get._3.get.split("\t", -1)
      fields.size must beEqualTo(PagePingWithContextSpec.expected.size)
      piiFields.size must beEqualTo(PagePingWithContextSpec.pii.size)
      Result.unit {
        for (idx <- PagePingWithContextSpec.expected.indices)
          fields(idx) must beFieldEqualTo(PagePingWithContextSpec.expected(idx), withIndex = idx)
        for (idx <- PagePingWithContextSpec.pii.indices)
          piiFields(idx) must beFieldEqualTo(PagePingWithContextSpec.pii(idx), withIndex = idx)
      }
    }
  }
}
