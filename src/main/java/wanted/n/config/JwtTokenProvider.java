package wanted.n.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import wanted.n.dto.TokenIssuanceDTO;
import wanted.n.enums.UserRole;
import wanted.n.service.UserDetailsServiceImpl;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${token.key}")
    private String issuer;
    private SecretKey secretKey;

    /* SecretKey 초기화 메서드 (빈 생성 시 1회 실행) */
    @PostConstruct
    public void init() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /* Access Token 생성 메서드 - 클레임에 이메일과 UserRole 삽입 */
    public String generateAccessToken(TokenIssuanceDTO tokenTokenIssuanceDTO) {
        Claims claims = Jwts.claims().setSubject(tokenTokenIssuanceDTO.getId().toString());
        claims.put("email", tokenTokenIssuanceDTO.getEmail());
        claims.put("userRole", tokenTokenIssuanceDTO.getUserRole().getRoleName());
        claims.put("account", tokenTokenIssuanceDTO.getAccount());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 기간 1시간
                .setExpiration(new Date(System.currentTimeMillis() + (60 * 60 * 1000)))
                .signWith(secretKey)
                .compact();
    }

    /* Refresh Token 생성 메서드 - 클레임에 이메일 삽입 (추후 엑세스 토큰 재발급 시 사용예정)*/
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // 유효 기간 24시간
                .setExpiration(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)))
                .signWith(secretKey)
                .compact();
    }

    /* 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /* 토큰에서 Id 추출 */
    public Long getIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    /* 토큰에서 이메일 추출 */
    public String getEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    /* 토큰에서 계정 추출 */
    public String getAccountFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("account", String.class);
    }

    /* 토큰 인증 */
    public Authentication getAuthentication(String token) {

        // "Bearer " 프리픽스 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 토큰을 파싱하여 클레임 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 클레임에서 이메일과 사용자 역할 가져오기
        String email = claims.get("email", String.class);
        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

        // 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 사용자 권한과 역할 권한을 병합하여 Authentication 객체 생성
        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        authorities.addAll(userRole.getAuthorities());

        return new UsernamePasswordAuthenticationToken(
                userDetails, "", authorities
        );
    }

}

