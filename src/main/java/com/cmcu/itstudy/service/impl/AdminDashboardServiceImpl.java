package com.cmcu.itstudy.service.impl;

import com.cmcu.itstudy.dto.admin.dashboard.AdminDashboardActiveDayDto;
import com.cmcu.itstudy.dto.admin.dashboard.AdminDashboardLatestUserDto;
import com.cmcu.itstudy.dto.admin.dashboard.AdminDashboardResponseDto;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.enums.ContributorRequestStatus;
import com.cmcu.itstudy.enums.DocumentStatus;
import com.cmcu.itstudy.repository.ContributorRequestRepository;
import com.cmcu.itstudy.repository.DocumentRepository;
import com.cmcu.itstudy.repository.DocumentViewRepository;
import com.cmcu.itstudy.repository.UserRepository;
import com.cmcu.itstudy.service.contract.AdminDashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final ContributorRequestRepository contributorRequestRepository;
    private final DocumentViewRepository documentViewRepository;

    public AdminDashboardServiceImpl(
            UserRepository userRepository,
            DocumentRepository documentRepository,
            ContributorRequestRepository contributorRequestRepository,
            DocumentViewRepository documentViewRepository
    ) {
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.contributorRequestRepository = contributorRequestRepository;
        this.documentViewRepository = documentViewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponseDto getDashboard() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startThisMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime startNextMonth = startThisMonth.plusMonths(1);
        LocalDateTime startLastMonth = startThisMonth.minusMonths(1);

        long totalUsers = userRepository.count();
        long usersThisMonth = userRepository.countCreatedBetween(startThisMonth, startNextMonth);
        long usersLastMonth = userRepository.countCreatedBetween(startLastMonth, startThisMonth);
        double userGrowthPercent = roundOne(monthOverMonthPercent(usersThisMonth, usersLastMonth));

        long totalDocuments = documentRepository.countByDeletedFalse();
        long docsThisMonth = documentRepository.countCreatedBetweenNotDeleted(startThisMonth, startNextMonth);
        long docsLastMonth = documentRepository.countCreatedBetweenNotDeleted(startLastMonth, startThisMonth);
        double documentGrowthPercent = roundOne(monthOverMonthPercent(docsThisMonth, docsLastMonth));

        long pendingDocs = documentRepository.countByStatusAndDeletedFalse(DocumentStatus.PENDING);
        long pendingContributor = contributorRequestRepository.countByStatus(ContributorRequestStatus.PENDING);
        long pendingRequests = pendingDocs + pendingContributor;

        LocalDate today = LocalDate.now();
        LocalDate startDay = today.minusDays(6);
        LocalDateTime since = startDay.atStartOfDay();
        List<AdminDashboardActiveDayDto> activeUsersByDay = buildLastSevenDaysActiveUsers(startDay, today, since);

        List<User> latest = userRepository.findTop5ByOrderByCreatedAtDesc();
        List<AdminDashboardLatestUserDto> latestUsers = new ArrayList<>(latest.size());
        for (User u : latest) {
            String name = u.getFullName() != null && !u.getFullName().isBlank()
                    ? u.getFullName().trim()
                    : u.getEmail();
            latestUsers.add(AdminDashboardLatestUserDto.builder()
                    .id(u.getId())
                    .name(name)
                    .createdAt(u.getCreatedAt().toLocalDate().toString())
                    .build());
        }

        return AdminDashboardResponseDto.builder()
                .totalUsers(totalUsers)
                .userGrowthPercent(userGrowthPercent)
                .totalDocuments(totalDocuments)
                .documentGrowthPercent(documentGrowthPercent)
                .pendingRequests(pendingRequests)
                .activeUsersByDay(activeUsersByDay)
                .latestUsers(latestUsers)
                .build();
    }

    private List<AdminDashboardActiveDayDto> buildLastSevenDaysActiveUsers(
            LocalDate startDay,
            LocalDate endDay,
            LocalDateTime since
    ) {
        List<Object[]> rows = documentViewRepository.countDistinctUsersByViewDaySince(since);
        Map<LocalDate, Long> byDay = new HashMap<>();
        if (rows != null) {
            for (Object[] row : rows) {
                if (row == null || row.length < 2) {
                    continue;
                }
                LocalDate d = toLocalDate(row[0]);
                if (d == null) {
                    continue;
                }
                long c = toLong(row[1]);
                byDay.put(d, c);
            }
        }
        List<AdminDashboardActiveDayDto> out = new ArrayList<>();
        for (LocalDate d = startDay; !d.isAfter(endDay); d = d.plusDays(1)) {
            long c = byDay.getOrDefault(d, 0L);
            out.add(AdminDashboardActiveDayDto.builder()
                    .date(d.toString())
                    .count(c)
                    .build());
        }
        return out;
    }

    private static LocalDate toLocalDate(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof LocalDate ld) {
            return ld;
        }
        if (o instanceof java.sql.Date d) {
            return d.toLocalDate();
        }
        if (o instanceof java.util.Date d) {
            return new java.sql.Date(d.getTime()).toLocalDate();
        }
        return null;
    }

    private static long toLong(Object o) {
        if (o == null) {
            return 0L;
        }
        if (o instanceof Number n) {
            return n.longValue();
        }
        return 0L;
    }

    private static double monthOverMonthPercent(long current, long previous) {
        if (previous <= 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) / (double) previous) * 100.0;
    }

    private static double roundOne(double v) {
        return BigDecimal.valueOf(v).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
