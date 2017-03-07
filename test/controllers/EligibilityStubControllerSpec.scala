/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.ssttp.desstub.controllers

import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future
import testData._

class EligibilityStubControllerSpec extends PlaySpec with Results {

  val controller = new EligibilityStubController()

  "Eligibility Controller" should {
    "reject requests with no authorization header for sa returns" in {
      val result: Future[Result] = controller.generateSAReturns("1234").apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      status(result) mustBe UNAUTHORIZED
      bodyText mustBe "No authorization header present"
    }

    "reject requests with no authorization header for communication preferences" in {
      val result: Future[Result] = controller.generateCommPreferences("1234").apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      status(result) mustBe UNAUTHORIZED
      bodyText mustBe "No authorization header present"
    }

    "reject requests with no authorization header for sa debits" in {
      val result: Future[Result] = controller.generateSADebits("1234").apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      status(result) mustBe UNAUTHORIZED
      bodyText mustBe "No authorization header present"
    }

    "return sa returns with valid credential id" in {
      val controller = new EligibilityStubController()
      val result: Future[Result] = controller.generateSAReturns("1234567890").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe OK
      bodyText mustBe saReturn
    }

    "return communication preferences with valid credential id" in {
      val result: Future[Result] = controller.generateCommPreferences("1234567890").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe OK
      bodyText mustBe commPreferences
    }

    "return sa debits with valid credential id" in {
      val result: Future[Result] = controller.generateSADebits("1234567890").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe OK
      bodyText mustBe saDebit
    }

    "return 400 your submission contains one or more errors invalid utr for sa returns" in {
      val result: Future[Result] = controller.generateSAReturns("123456789").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe BAD_REQUEST
      bodyText must include(yourSubmissionError)
    }

    "return 400 your submission contains one or more errors invalid utr for communication preferences" in {
      val result: Future[Result] = controller.generateCommPreferences("123456789").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe BAD_REQUEST
      bodyText must include(yourSubmissionError)
    }

    "return 400 your submission contains one or more errors with invalid utr for sa debits" in {
      val result: Future[Result] = controller.generateSADebits("123456789").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe BAD_REQUEST
      bodyText must include(yourSubmissionError)
    }

    "return 404 with unknown utr for sa returns" in {
      val result: Future[Result] = controller.generateSAReturns("0000000000").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe NOT_FOUND
      bodyText mustBe ""
    }

    "return 404 with unknown utr for communication preferences" in {
      val result: Future[Result] = controller.generateCommPreferences("0000000000").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe NOT_FOUND
      bodyText mustBe ""
    }

    "return 404 with unknown utr for sa debits" in {
      val result: Future[Result] = controller.generateSADebits("0000000000").apply(fakeAuthRequest)
      val bodyText: String = contentAsString(result)
      status(result) mustBe NOT_FOUND
      bodyText mustBe ""
    }
  }
}
