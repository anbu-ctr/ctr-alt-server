package org.example.controller;

import org.example.dto.AddMemberRequest;
import org.example.dto.ApiResponse;
import org.example.dto.GetMembersRequest;
import org.example.dto.MemberListDto;
import org.example.entity.Member;
import org.example.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMember(@RequestBody final AddMemberRequest request) {
        try {
            final Member member = memberService.addMember(request);
            final Map<String, Object> data = new HashMap<>();

            data.put("message", "Member added successfully");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse> getAllMembers(@RequestBody final GetMembersRequest request) {
        try {
            if (!"get".equals(request.getType())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid request type"));
            }

            final int start = request.getData().getStart();
            final int limit = request.getData().getLimit();
            final Integer status = request.getData().getStatus();
            final List<MemberListDto> members = memberService.getAllMembers(start, limit, status);
            final long totalCount = memberService.getTotalMembersCount(status);
            final Map<String, Object> data = new HashMap<>();

            data.put("members", members);
            data.put("totalCount", totalCount);
            data.put("hasMore", (start - 1 + limit) < totalCount);

            return ResponseEntity.ok(ApiResponse.success(data));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}