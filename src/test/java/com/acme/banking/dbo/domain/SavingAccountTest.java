package com.acme.banking.dbo.domain;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SavingAccountTest {

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorArgumentsAreValid() {
        UUID id = UUID.randomUUID();
        double amount = 1.0d;
        Client client = new Client(UUID.randomUUID(), randomAlphabetic(32));

        SavingAccount actual = new SavingAccount(id, client, amount);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("client", client);
        // Type of double is erased in assertj chain, so we have to use separate assert
        assertThat(actual.getAmount()).isEqualTo(amount, Offset.offset(0.01d));
    }

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorCalledWithNullId() {
        UUID id = null;
        double amount = 1.0d;
        Client client = new Client(UUID.randomUUID(), randomAlphabetic(32));

        assertThatThrownBy(() -> new SavingAccount(id, client, amount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("id must not be null");
    }

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorCalledWithNullClient() {
        UUID id = UUID.randomUUID();
        double amount = 1.0d;
        Client client = null;

        assertThatThrownBy(() -> new SavingAccount(id, client, amount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("client must not be null");
    }

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorCalledWithNanAmount() {
        UUID id = UUID.randomUUID();
        double amount = Double.NaN;
        Client client = new Client(UUID.randomUUID(), randomAlphabetic(32));

        assertThatThrownBy(() -> new SavingAccount(id, client, amount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("amount must not be NaN");
    }

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorCalledWithNegativeAmount() {
        UUID id = UUID.randomUUID();
        double amount = -1.0d;
        Client client = new Client(UUID.randomUUID(), randomAlphabetic(32));

        assertThatThrownBy(() -> new SavingAccount(id, client, amount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("amount must not be negative or zero");
    }

    @Test
    public void shouldCreateNewSavingAccount_when_sutConstructorCalledWithZeroAmount() {
        UUID id = UUID.randomUUID();
        double amount = 0.0d;
        Client client = new Client(UUID.randomUUID(), randomAlphabetic(32));

        assertThatThrownBy(() -> new SavingAccount(id, client, amount))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("amount must not be negative or zero");
    }

    @Test
    void withdrawShouldReturnNewSavingAccountWithAmountLessThanOriginalByGivenAmount() {
        SavingAccount savingAccount1 = new SavingAccount(
                UUID.randomUUID(),
                new Client(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(16)),
                3.0d);
        double amount = 1.5d;
        double expectedAmount = savingAccount1.getAmount() - amount;

        SavingAccount savingAccount2 = savingAccount1.withdraw(amount);

        assertThat(savingAccount2).isNotNull().isNotSameAs(savingAccount1)
                .hasFieldOrPropertyWithValue("id", savingAccount1.getId())
                .hasFieldOrPropertyWithValue("client", savingAccount1.getClient())
                .hasFieldOrPropertyWithValue("amount", expectedAmount);
//        assertEquals(expectedAmount, savingAccount2.getAmount(), 0.01d);
    }

    @Test
    void depositShouldReturnNewSavingAccountWithAmountMoreThanOriginalByGivenAmount() {
        SavingAccount savingAccount1 = new SavingAccount(
                UUID.randomUUID(),
                new Client(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(16)),
                3.0d);
        double amount = 1.5d;
        double expectedAmount = savingAccount1.getAmount() + amount;

        SavingAccount savingAccount2 = savingAccount1.deposit(amount);

        assertThat(savingAccount2).isNotNull().isNotSameAs(savingAccount1)
                .hasFieldOrPropertyWithValue("id", savingAccount1.getId())
                .hasFieldOrPropertyWithValue("client", savingAccount1.getClient())
                .hasFieldOrPropertyWithValue("amount", expectedAmount);
//        assertEquals(expectedAmount, savingAccount2.getAmount(), 0.01d);
    }
}