package hng_java_boilerplate.aboutpage.service;

import hng_java_boilerplate.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.aboutpage.entity.AboutPageContent;
import hng_java_boilerplate.aboutpage.repository.AboutPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AboutPageService {
    private final AboutPageRepository aboutPageRepository;

    @Transactional
    public void updateAboutPageContent(AboutPageContentDto contentDto) {
        AboutPageContent content = new AboutPageContent();
        content.setTitle(contentDto.getTitle());
        content.setIntroduction(contentDto.getIntroduction());

        Map<String, Object> customSections = contentDto.getCustomSections();
        if (customSections != null) {
            Map<String, Object> stats = (Map<String, Object>) customSections.get("stats");
            if (stats != null) {
                content.setYearsInBusiness((Integer) stats.get("years_in_business"));
                content.setCustomers((Integer) stats.get("customers"));
                content.setMonthlyBlogReaders((Integer) stats.get("monthly_blog_readers"));
                content.setSocialFollowers((Integer) stats.get("social_followers"));
            }

            Map<String, Object> services = (Map<String, Object>) customSections.get("services");
            if (services != null) {
                content.setServicesTitle((String) services.get("title"));
                content.setServicesDescription((String) services.get("description"));
            }
        }

        aboutPageRepository.save(content);
    }

    @Transactional(readOnly = true)
    public AboutPageContentDto getAboutPageContent() throws AccessDeniedException {
        AboutPageContent content = aboutPageRepository.findById(1L).orElseThrow(() -> new AccessDeniedException("Access denied"));

        AboutPageContentDto contentDto = new AboutPageContentDto();
        contentDto.setTitle(content.getTitle());
        contentDto.setIntroduction(content.getIntroduction());

        Map<String, Object> customSections = new HashMap<>();
        Map<String, Object> stats = new HashMap<>();
        stats.put("years_in_business", content.getYearsInBusiness());
        stats.put("customers", content.getCustomers());
        stats.put("monthly_blog_readers", content.getMonthlyBlogReaders());
        stats.put("social_followers", content.getSocialFollowers());

        customSections.put("stats", stats);

        Map<String, Object> services = new HashMap<>();
        services.put("title", content.getServicesTitle());
        services.put("description", content.getServicesDescription());

        customSections.put("services", services);

        contentDto.setCustomSections(customSections);

        return contentDto;
    }
}
