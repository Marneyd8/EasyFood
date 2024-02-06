package cz.muni.fi.pv168.project;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The example unit tests
 */
final class MainTest {

    @Test
    void example() {
        assertThat(Integer.MAX_VALUE)
                .as("This test should always pass")
                .isEqualTo(2_147_483_647);
    }
}
