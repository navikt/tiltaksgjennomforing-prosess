package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
@DirtiesContext
public class StsServiceIntTest {

    @Autowired
    private StsService stsService;

    @Test
    public void henter_token() {
        String token = stsService.hentToken();
        assertEquals("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aWx0YWtzZ2plbm5vbWZvcmluZy1wcm9zZXNzIiwiYXVkIjoiYXVkLXN5c3RlbSIsInZlciI6IjEuMCIsIm5iZiI6MTU3ODU3NjQ0OCwiYXV0aF90aW1lIjoxNTc4NTc2NDQ4LCJpc3MiOiJzeXN0ZW0iLCJleHAiOjIzNTYxNzY0NDgsIm5vbmNlIjoibXlOb25jZSIsImlhdCI6MTU3ODU3NjQ0OCwianRpIjoiYjE3NTZlZGUtNjQxNy00ZTk2LTg5ODYtM2M5NzllOTkxMzEzIiwiYWNyIjoiTGV2ZWw0In0.UDf2r5K93OrEotGGbdxIh5kFI8UdlQicRdrgw4GnpvaHYtgwWrvaCP6f81HylFg5PoLdJp1HHuWygOp1SpPU_SFPA5zdUST8lUHvEeLxxXgu9qiorLzMJL2NGzB06jxL-6yNC_WqHz0BdG_qKcRcV27D2ZVIWmILcPumLW8JwB3gdmFkvO0pnXQ1LG4X_cMt8sps5u3FbH-1SVIqRr8DAA7dxioRGrCqJopJIZ76JNXFvATmAmmE6LWGSVaf40sALSd4uZ7NiXZWgF4kDfCGt7q48xyfc_CLTUBHszh6seEGAg6WW228xXLAWmHlcjyQkmol8F0xs3LN2cA7A6xLLg", token);
    }

}
