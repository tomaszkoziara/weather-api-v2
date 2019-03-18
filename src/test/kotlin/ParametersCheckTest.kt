import common.InputError
import common.startEndParameterCheckFun
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import routing.ApplicationCallWrapper

class ParametersCheckTest: DescribeSpec() {

    init {

        describe("startEndParameterCheckFun") {

            it("should throw an error if parameter start doesn't exist") {

                val applicationCall: ApplicationCallWrapper = mockk()
                every { applicationCall.queryParameter("start") } returns null
                every { applicationCall.queryParameter("end") } returns "2008-09-15T00:00:00Z"

                val exception = shouldThrowExactly<InputError> { startEndParameterCheckFun(applicationCall) }
                exception.message shouldBe "Parameter 'start' is missing!"
            }

            it("should throw an error if parameter end doesn't exist") {

                val applicationCall: ApplicationCallWrapper = mockk()
                every { applicationCall.queryParameter("start") } returns "2008-09-15T00:00:00Z"
                every { applicationCall.queryParameter("end") } returns null

                val exception = shouldThrowExactly<InputError> { startEndParameterCheckFun(applicationCall) }
                exception.message shouldBe "Parameter 'end' is missing!"
            }

            it("should throw an error if parameter start is not a valid ISO8601") {

                val applicationCall: ApplicationCallWrapper = mockk()
                every { applicationCall.queryParameter("start") } returns "2008/09/15"
                every { applicationCall.queryParameter("end") } returns "2008-09-16T00:00:00Z"

                val exception = shouldThrowExactly<InputError> { startEndParameterCheckFun(applicationCall) }
                exception.message shouldBe "Parameter 'start' is not a valid ISO8601 parameter!"
            }

            it("should throw an error if parameter end is not a valid ISO8601") {

                val applicationCall: ApplicationCallWrapper = mockk()
                every { applicationCall.queryParameter("start") } returns "2008-09-15T00:00:00Z"
                every { applicationCall.queryParameter("end") } returns "2008/09/16"

                val exception = shouldThrowExactly<InputError> { startEndParameterCheckFun(applicationCall) }
                exception.message shouldBe "Parameter 'end' is not a valid ISO8601 parameter!"
            }

        }

    }

}