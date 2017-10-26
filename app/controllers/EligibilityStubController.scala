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

import javax.inject.Inject


import play.api.mvc.{Action, AnyContent}

class EligibilityStubController @Inject()() extends ResponseHandling {

  /**
    * Represents the getSAReturns endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateSAReturns(utr: String): Action[AnyContent] =
    serveFile(pickFileSaReturns(utr))(utr)

  def pickFileSaReturns(utr: String) = utr match{
    case "9370940447" => "/SAReturnsNotSubmitted.json"
    case _ => "/SAReturnHappy.json"
  }
  /**
    * Represents the getSADebits endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateSADebits(utr: String): Action[AnyContent]= {
    serveFile(pickFileSaDebits(utr))(utr)
  }

  def pickFileSaDebits(utr: String) = utr match{
    case "4534690744" => "/SADebitUnHappyInsignificantDept.json"
    case "9446580253" =>  "/SaDebitUnHappyTooMuch.json"
    case "6068021487" =>  "/SaDeptTooOld.json"
    case "9370940447" =>  "/SaDebitNotSubmittedTaxYear.json"
    case _ => "/SADebitHappy.json"
  }

  /**
    * Represents the getCommPreferences endpoint for DES which is called by Time To Pay Taxpayer
    */
  def generateCommPreferences(utr: String): Action[AnyContent] =
    serveFile("/CommPreferences.json")(utr)
}
