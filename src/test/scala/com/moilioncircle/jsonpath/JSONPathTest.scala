package com.moilioncircle.jsonpath

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

/**
 * Created by leon on 15-6-1.
 */
@RunWith(classOf[JUnitRunner])
class JSONPathTest extends FunSuite {
  test("json parser 10") {
    val json = "[\"\\u9648\"]"
    val parser = JSONParser(json)
    val value = parser.parse()
    assert(value == JSONArray(List("陈")))

    {
      val json = "[\"\\u964"
      val parser = JSONParser(json)
      try {
        parser.parse()
        fail()
      } catch {
        case e: JSONSyntaxException =>
      }
    }

    {
      val json = ""
      val parser = JSONParser(json)
      try {
        parser.parse()
        fail()
      } catch {
        case e: JSONSyntaxException =>
      }
    }
  }

  test("json parser") {
    val json =
      """
        |[
        |    155e+012,
        |    2,
        |    3,
        |    [
        |        true,
        |        false,
        |        null
        |    ],
        |    {
        |        "a\u9648bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val parser = JSONParser(json)
    parser.parse() match {
      case JSONArray(list: List[Any]) => assert(list.toString === "List(1.55E+14, 2, 3, JSONArray(List(true, false, null)), JSONObject(Map(a陈bc -> 1.233E-10, bcd -> true, c\rde -> null)), true, false, null)")
    }
  }

  test("json parser 1") {
    val json =
      """
        |[
        |    155e+012,
        |    0.55,
        |    0,
        |    9,
        |    100,
        |    110e+12,
        |    110e12,
        |    110e-12,
        |    3,
        |    -100,
        |    [
        |        true,
        |        false,
        |        nulul
        |    ],
        |    {
        |        "a泉bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONLexerException => //pass test
    }
  }

  test("json parser 2") {
    val json =
      """
        |[
        |    155e+012,
        |    2,
        |    3,
        |    [
        |        truue,
        |        false,
        |        null
        |    ],
        |    {
        |        "a泉bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONLexerException => //pass test
    }
  }

  test("json parser 3") {
    val json =
      """
        |[
        |    155e+012,
        |    2,
        |    3,
        |    [
        |        true,
        |        falsse,
        |        null
        |    ],
        |    {
        |        "a泉bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONLexerException => //pass test
    }
  }

  test("json parser 4") {
    val json =
      """
        |[
        |    1a5e+012,
        |    2,
        |    3,
        |    [
        |        true,
        |        falsse,
        |        null
        |    ],
        |    {
        |        "a泉bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }
  }

  test("json parser 5") {
    val json =
      """
        |{
        |]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }
  }

  test("json parser 6") {
    val json =
      """
        |true
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }
  }

  test("json parser 7") {
    val json =
      """
        |{"a":true "b":false}
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }

    val json1 =
      """
        |{"a":true,"b":false
      """.stripMargin
    val parser1 = JSONParser(json1)
    try {
      parser1.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }

    val json2 =
      """
        |[true,false
      """.stripMargin
    val parser2 = JSONParser(json2)
    try {
      parser2.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }

    val json3 = "[true,fals "
    val parser3 = JSONParser(json3)
    try {
      parser3.parse()
      fail()
    } catch {
      case e: JSONLexerException => //pass test
    }

    val json4 = "[true,f,true] "
    val parser4 = JSONParser(json4)
    try {
      parser4.parse()
      fail()
    } catch {
      case e: JSONLexerException => //pass test
    }
    {
      val json4 = "[true,fa,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[true,fal,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[true,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[t,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[tr,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[tru,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[n,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[nu,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[nul,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONLexerException => //pass test
      }
    }
    {
      val json4 = "[a,fals,true] "
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONSyntaxException => //pass test
      }
    }
    {
      val json4 = "[true,false a"
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONSyntaxException => //pass test
      }
    }
    {
      val json4 = "{ \"key1\":true,\"key2\":false a"
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONSyntaxException => //pass test
      }
    }
    {
      val json4 = "[true]"
      val parser4 = JSONParser(json4)
      val value = parser4.parse()
      assert(value === JSONArray(List(true)))
    }
    {
      val json4 = "{\"key\" true}"
      val parser4 = JSONParser(json4)
      try {
        parser4.parse()
        fail()
      } catch {
        case e: JSONSyntaxException => //pass test
      }
    }
    {
      val json4 = "[true,false\r\n\t,true] "
      val parser4 = JSONParser(json4)
      val value = parser4.parse()
      assert(value == JSONArray(List(true, false, true)))
    }
  }

  test("json parser 8") {
    val json =
      """
        |[true false]
      """.stripMargin
    val parser = JSONParser(json)
    try {
      parser.parse()
      fail()
    } catch {
      case e: JSONSyntaxException => //pass test
    }
  }

  test("json parser 9") {
    val json =
      """
        |[]
      """.stripMargin
    val parser = JSONParser(json)
    val value = parser.parse()
    assert(value == JSONArray(List()))

    val json1 =
      """
        |{}
      """.stripMargin
    val parser1 = JSONParser(json1)
    val value1 = parser1.parse()
    assert(value1 == JSONObject(Map()))
  }

  test("json pointer parser") {
    var str = "/abcd/0~00/0123[*]/~0~1"
    var jp = JSONPointerParser(str)
    var rules: List[Rule] = jp.parsePath()
    assert(rules == List(Rule("abcd"), Rule("0~0"), Rule("0123[*]"), Rule("~/")))

    str = "/abcd"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("abcd")))

    str = "/0"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("0")))

    str = "/012"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("012")))

    str = "/12"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("12")))

    str = "/"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("")))

    str = "/~0"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("~")))

    str = ""
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List())

    str = "/"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("")))

    str = "/~"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("~")))

    str = "/~1"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("/")))

    str = "/12~0~1"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("12~/")))

    str = "/0~1/"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("0/"), Rule("")))

    try {
      str = "../../../"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = "./../../"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = "./"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = "../"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    str = "/0"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("0")))

    str = "//"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule(""), Rule("")))

    str = "/0/1"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("0"), Rule("1")))

    str = "/0/~abc"
    jp = JSONPointerParser(str)
    rules = jp.parsePath()
    assert(rules == List(Rule("0"), Rule("~abc")))

    try {
      str = "../."
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = "../..abc"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = "../.abc"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException => // PASS
    }

    try {
      str = ".../"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException =>
    }

    try {
      str = ".a"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException =>
    }

    try {
      str = "."
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException =>
    }

    try {
      str = ".."
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException =>
    }

    try {
      str = "a"
      jp = JSONPointerParser(str)
      rules = jp.parsePath()
      fail()
    } catch {
      case e: JSONPointerSyntaxException =>
    }
  }

  test("json pointer") {
    val json =
      """
        |{
        |  "store": {
        |    "book": [
        |      { "category": {"reference":true},
        |        "author": "Nigel Rees",
        |        "title": "Sayings of the Century",
        |        "price": 8.95
        |      },
        |      { "category": "fiction",
        |        "author": "Evelyn Waugh",
        |        "title": "Sword of Honour",
        |        "price": 12.99
        |      },
        |      { "category": "fiction",
        |        "author": "Herman Melville",
        |        "title": "Moby Dick",
        |        "isbn": "0-553-21311-3",
        |        "price": 8.99
        |      },
        |      { "category": "fiction",
        |        "author": "J. R. R. Tolkien",
        |        "title": "The Lord\r\F\f\n\b\t\\\"\/\u9648\g of the Rings",
        |        "isbn": "0-395-19395-8",
        |        "price": 22.99
        |      }
        |    ],
        |    "bicycle": {
        |      "color": "red",
        |      "price": 19.95
        |    }
        |  }
        |}
      """.stripMargin
    val jp = JSONPointer(json)
    val value = jp.read[List[Any]]("/store/book/*/isbn")
    assert(value === Some(List("0-553-21311-3", "0-395-19395-8")))

    val value1 = jp.read[List[Any]]("/store/book/1:2/isbn")
    assert(value1 === Some("0-553-21311-3"))

    val value2 = jp.read[String]("/store/bicycle/color")
    assert(value2 === Some("red"))

    val valuen = jp.read[List[String]]("/store/book/3:2/isbn")
    assert(valuen === Some(List("0-395-19395-8", "0-553-21311-3")))

    try {
      jp.read("/store/book/abc/isbn")
      fail()
    } catch {
      case e: JSONPointerException =>
    }

    try {
      jp.read("/store/book/1:2:3/isbn")
      fail()
    } catch {
      case e: JSONPointerException =>
    }

    try {
      jp.read("/store/book/1,a,b/isbn")
      fail()
    } catch {
      case e: JSONPointerException =>
    }

    val value4 = jp.read[List[Any]]("/store/book/*/category,isbn")
    assert(value4 === Some(List(JSONObject(Map("reference" -> true)), "fiction", List("fiction", "0-553-21311-3"), List("fiction", "0-395-19395-8"))))

    val value5 = jp.read[List[Any]]("/store/book/*/*")
    assert(value5 === Some(List(List(JSONObject(Map("reference" -> true)), "Nigel Rees", "Sayings of the Century", 8.95), List("fiction", "Evelyn Waugh", "Sword of Honour", 12.99), List("Herman Melville", 8.99, "0-553-21311-3", "fiction", "Moby Dick"), List("J. R. R. Tolkien", 22.99, "0-395-19395-8", "fiction", "The Lord\r\f\f\n\b\t\\\"/陈\\g of the Rings"))))

  }

  test("json pointer 1") {
    val json =
      """
        |    {
        |        "a泉bc": 1.233e10,
        |        "bcd": true,
        |        "c\rde": null
        |    }
      """.stripMargin
    val jp = JSONPointer(json)
    val value = jp.read[Boolean]("/bcd")
    assert(value === Some(true))
  }

  test("json pointer 2") {
    val json =
      """
        |[
        |    155e+012,
        |    2,
        |    "bcd",
        |    [
        |        true,
        |        false,
        |        null
        |    ],
        |    {
        |        "a\u9648bc": 1.233e+10,
        |        "bcd": [true,false],
        |        "c\rde": null,
        |        "0":"object"
        |    },
        |    [
        |        true,
        |        "abc",
        |        null
        |    ],
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin
    val jp = JSONPointer(json)
    val value = jp.read[Double]("/4/a陈bc")
    assert(value === Some(1.233e+10))

    val value1 = jp.read[List[Any]]("/*/0")
    assert(value1 === Some(List(true, "object", true)))

    val value2 = jp.read[List[Any]]("/*/1")
    assert(value2 === Some(List(false, "abc")))

    val value3 = jp.read[Boolean]("/4/bcd/0")
    assert(value3 === Some(true))

    try {
      jp.read("/a:b/0")
      fail()
    } catch {
      case e: JSONPointerException =>
    }

    try {
      jp.read("/1,/0")
      fail()
    } catch {
      case e: JSONPointerException =>
    }

  }

  test("json pointer reduce") {
    val json =
      """
        |{
        |  "store": {
        |    "book": [
        |      { "category": {"reference":true},
        |        "author": "Nigel Rees",
        |        "title": "Sayings of the Century",
        |        "price": 8.95
        |      },
        |      { "category": "fiction",
        |        "author": "Evelyn Waugh",
        |        "title": "Sword of Honour",
        |        "price": 12.99
        |      },
        |      { "category": "fiction",
        |        "author": "Herman Melville",
        |        "title": "Moby Dick",
        |        "isbn": "0-553-21311-3",
        |        "price": 8.99
        |      },
        |      { "category": "fiction",
        |        "author": "J. R. R. Tolkien",
        |        "title": "The Lord\r\F\f\n\b\t\\\"\/\u9648\g of the Rings",
        |        "isbn": "0-395-19395-8",
        |        "price": 22.99
        |      }
        |    ],
        |    "bicycle": {
        |      "color": "red",
        |      "price": 19.95
        |    }
        |  }
        |}
      """.stripMargin
    val jp = JSONPointer(json)
    val value = jp.read[List[_]]("/store/book/*/isbn,price")
    assert(value === Some(List(8.95, 12.99, List("0-553-21311-3", 8.99), List("0-395-19395-8", 22.99))))

    val value1 = jp.read[String]("/store/book/1:2/isbn")
    assert(value1 === Some("0-553-21311-3"))

    val value2 = jp.read[Any]("/store/book/1:2/abc")
    assert(value2 === None)

  }

  test("json pointer read") {
    val json =
      """
        |{
        |  "store": {
        |    "book": [
        |      { "category": {"reference":true},
        |        "author": "Nigel Rees",
        |        "title": "Sayings of the Century",
        |        "price": 8.95
        |      },
        |      { "category": "fiction",
        |        "author": "Evelyn Waugh",
        |        "title": "Sword of Honour",
        |        "price": 12.99
        |      },
        |      { "category": "fiction",
        |        "author": "Herman Melville",
        |        "title": "Moby Dick",
        |        "isbn": "0-553-21311-3",
        |        "price": 8.99
        |      },
        |      { "category": "fiction",
        |        "author": "J. R. R. Tolkien",
        |        "title": "The Lord\r\F\f\n\b\t\\\"\/\u9648\g of the Rings",
        |        "isbn": "0-395-19395-8",
        |        "price": 22.99
        |      }
        |    ],
        |    "bicycle": {
        |      "color": "red",
        |      "price": 19.95,
        |      "..": false
        |    }
        |  }
        |}
      """.stripMargin
    val jp = JSONPointer(json)
    jp.read("/store/book/3")

    val value4 = jp.read[Double]("/store/bicycle/price")
    assert(value4 === Some(19.95))

    {
      val json = "[]"
      val jp = JSONPointer(json)
      val value8 = jp.read[JSONArray]("")
      assert(value8 === Some(JSONArray(List.empty)))
    }
  }

  test("RFC 6901") {
    val json =
      """
        |{
        |    "foo": ["bar", "baz"],
        |    "": 0,
        |    "a/b": 1,
        |    "c%d": 2,
        |    "e^f": 3,
        |    "g|h": 4,
        |    "i\\j": 5,
        |    "k\"l": 6,
        |    " ": 7,
        |    "m~n": 8,
        |    "0,2":9,
        |    "0-2":10,
        |    "*":11
        |}
      """.stripMargin
    val jp = JSONPointer(json)
    var value: Any = jp.read[JSONObject]("")
    assert(value === Some(JSONObject(Map("" -> 0, "*" -> 11, "c%d" -> 2, "i\\j" -> 5, "0,2" -> 9, "m~n" -> 8, "a/b" -> 1, " " -> 7, "g|h" -> 4, "0-2" -> 10, "k\"l" -> 6, "e^f" -> 3, "foo" -> JSONArray(List("bar", "baz"))))))

    value = jp.read[JSONArray]("/foo")
    assert(value === Some(JSONArray(List("bar", "baz"))))

    value = jp.read[String]("/foo/0")
    assert(value === Some("bar"))

    value = jp.read[Int]("/")
    assert(value === Some(0))

    value = jp.read[Int]("/a~1b")
    assert(value === Some(1))

    value = jp.read[Int]("/c%d")
    assert(value === Some(2))

    value = jp.read[Int]("/e^f")
    assert(value === Some(3))

    value = jp.read[Int]("/g|h")
    assert(value === Some(4))

    value = jp.read[Int]("/i\\j")
    assert(value === Some(5))

    value = jp.read[Int]("/k\"l")
    assert(value === Some(6))

    value = jp.read[Int]("/ ")
    assert(value === Some(7))

    value = jp.read[Int]("/m~0n")
    assert(value === Some(8))

    value = jp.read[Int]("/0~,2")
    assert(value === Some(9))

    value = jp.read[Int]("/0-2")
    assert(value === Some(10))

    value = jp.read[Int]("/~*")
    assert(value === Some(11))

  }

  test("json pointer neg number") {
    val json =
      """
        |[
        |    155e+012,
        |    0.55,
        |    0,
        |    9,
        |    100,
        |    110e+12,
        |    110e12,
        |    110e-12,
        |    3,
        |    -100,
        |    [
        |        true,
        |        false,
        |        null
        |    ],
        |    {
        |        "a泉bc": 1.233e-10,
        |        "bcd": true,
        |        "c\rde": null
        |    },
        |    true,
        |    false,
        |    null
        |]
      """.stripMargin

    {
      val jp = JSONPointer(json)
      val value = jp.read[Boolean]("/-2")
      assert(value === Some(false))
    }
    {
      val jp = JSONPointer(json)
      val value = jp.read[List[Any]]("/-2,-1")
      assert(value === Some(List(false, null)))
    }
    {
      val jp = JSONPointer(json)
      val value = jp.read[List[Any]]("/:")
      assert(value === Some(List(1.55E14, 0.55, 0, 9, 100, 1.1E14, 1.1E14, 1.1E-10, 3, -100, JSONArray(List(true, false, null)), JSONObject(Map("a泉bc" -> 1.233E-10, "bcd" -> true, "c\rde" -> null)), true, false, null)))
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/9a")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/9a,9b,")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/-")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/-01")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/100")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      try {
        val jp = JSONPointer(json)
        jp.read("/ , , ")
        fail()
      } catch {
        case e: JSONPointerException => // PASS
      }
    }
    {
      val jp = JSONPointer(json)
      val value = jp.read[List[Any]]("/-1,5,-2")
      assert(value === Some(List(null, 1.1E14, false)))
    }
    {
      val value = JSONPointer().read[Double]("/0", json)
      assert(value === Some(155e+012))
    }
    {
      val jsonObj = JSONParser(json).parse()
      assert(JSONPointer().read[Double]("/0", jsonObj) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double]("/0", json.iterator) == Some(155e+012))
    }
    {
      val jsonObj = JSONParser(json).parse()
      assert(JSONPointer().read[Double]("/0", jsonObj) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double]("/0", json.iterator) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double]("/0", json) == Some(155e+012))
    }
    {
      try {
        JSONPointer().read("/0")
        fail()
      } catch {
        case e: IllegalArgumentException =>
      }
    }
    {
      try {
        JSONPointer().read("../", JSONParser(json).parse())
        fail()
      } catch {
        case e: JSONPointerSyntaxException =>
      }
    }
    {
      val value = JSONPointer().read[Double](new Path / "0", json)
      assert(value === Some(155e+012))
    }
    {
      val jsonObj = JSONParser(json).parse()
      assert(JSONPointer().read[Double](new Path / "0", jsonObj) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double](new Path / "0", json.iterator) == Some(155e+012))
    }
    {
      assert(JSONPointer(json).read[Double](new Path / "0") == Some(155e+012))
    }
    {
      val jsonObj = JSONParser(json).parse()
      assert(JSONPointer().read[Double](new Path / "0", jsonObj) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double](new Path / "0", json.iterator) == Some(155e+012))
    }
    {
      assert(JSONPointer().read[Double](new Path / "0", json) == Some(155e+012))
    }
    {
      assert(JSONPointer(json).read[Double](new Path / "0") == Some(155e+012))
    }
    {
      assert(JSONPointer(json).read[Any](new Path / -1) == Some(null))
    }
  }

}