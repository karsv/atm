package com.example.atm.util;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class CardNumberGenerator {
    private static final Integer CARD_LENGTH = 16;
    private final Random random = new Random();

    public String randomCardGenerate() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < CARD_LENGTH; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        int checkDigit = this.getLastNumberOfCard(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    private int getLastNumberOfCard(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Integer.parseInt(number.substring(i, (i + 1)));
            if (i % 2 == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }
        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }
}
