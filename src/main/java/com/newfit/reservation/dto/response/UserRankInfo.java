package com.newfit.reservation.dto.response;

import com.newfit.reservation.domain.Authority;
import com.newfit.reservation.domain.Credit;
import lombok.Getter;

@Getter
public class UserRankInfo {
    private final Long rank;
    private final String nickname;
    private final Long amount;

    public UserRankInfo(Credit credit, Long rank) {
        this.rank = rank;
        this.nickname = credit.getAuthority().getUser().getNickname();
        this.amount = credit.getAmount();
    }

    public UserRankInfo(CreditRanking creditRanking) {
        this.rank = creditRanking.getRank();
        this.nickname = creditRanking.getAuthority().getUser().getNickname();
        this.amount = creditRanking.getAmount();
    }

    private UserRankInfo(Authority authority) {
        this.rank = 0L;
        this.nickname = authority.getUser().getNickname();
        this.amount = 0L;
    }

    public static UserRankInfo noCredit(Authority authority) {
        return new UserRankInfo(authority);
    }
}
