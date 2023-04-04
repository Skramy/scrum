import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SampleServiceTest {
    private lateinit var service: SampleService

    @BeforeEach
    fun configureSystemUnderTest() {
        service = SampleService()
    }

    @Test
    @DisplayName("Should return the correct message")
    fun shouldReturnCorrectMessage() {
        service.getMessage().also {
            assertThat(it).isEqualTo("Hello world!")
        }
    }
}