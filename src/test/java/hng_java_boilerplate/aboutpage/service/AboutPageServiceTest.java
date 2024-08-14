package hng_java_boilerplate.aboutpage.service;

import hng_java_boilerplate.aboutpage.dto.AboutPageContentDto;
import hng_java_boilerplate.aboutpage.entity.AboutPageContent;
import hng_java_boilerplate.aboutpage.repository.AboutPageRepository;
import hng_java_boilerplate.aboutpage.service.AboutPageService;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AboutPageServiceTest {

    @Mock
    private AboutPageRepository aboutPageRepository;

    @InjectMocks
    private AboutPageService aboutPageService;

    @Mock
    private ProductErrorHandler productErrorHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateAboutPageContent() {
        // Prepare the input DTO
        AboutPageContentDto contentDto = new AboutPageContentDto();
        contentDto.setTitle("More Than Just A BoilerPlate");
        contentDto.setIntroduction("Welcome to Hng Boilerplate, where passion meets innovation.");

        Map<String, Object> customSections = new HashMap<>();
        Map<String, Object> stats = new HashMap<>();
        stats.put("years_in_business", 10);
        stats.put("customers", 75000);
        stats.put("monthly_blog_readers", 100000);
        stats.put("social_followers", 1200000);
        customSections.put("stats", stats);

        Map<String, Object> services = new HashMap<>();
        services.put("title", "Trained to Give You The Best");
        services.put("description", "Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.");
        customSections.put("services", services);

        contentDto.setCustomSections(customSections);

        // Call the method to test
        aboutPageService.updateAboutPageContent(contentDto);

        // Verify interactions with the repository
        ArgumentCaptor<AboutPageContent> captor = ArgumentCaptor.forClass(AboutPageContent.class);
        verify(aboutPageRepository).save(captor.capture());

        AboutPageContent savedContent = captor.getValue();
        assertEquals("More Than Just A BoilerPlate", savedContent.getTitle());
        assertEquals("Welcome to Hng Boilerplate, where passion meets innovation.", savedContent.getIntroduction());
        assertEquals(10, savedContent.getYearsInBusiness());
        assertEquals(75000, savedContent.getCustomers());
        assertEquals(100000, savedContent.getMonthlyBlogReaders());
        assertEquals(1200000, savedContent.getSocialFollowers());
        assertEquals("Trained to Give You The Best", savedContent.getServicesTitle());
        assertEquals("Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.", savedContent.getServicesDescription());
    }

    @Test
    public void testGetAboutPageContent() {
        // Prepare the mock entity
        AboutPageContent content = new AboutPageContent();
        content.setTitle("More Than Just A BoilerPlate");
        content.setIntroduction("Welcome to Hng Boilerplate, where passion meets innovation.");
        content.setYearsInBusiness(10);
        content.setCustomers(75000);
        content.setMonthlyBlogReaders(100000);
        content.setSocialFollowers(1200000);
        content.setServicesTitle("Trained to Give You The Best");
        content.setServicesDescription("Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.");

        // Mock repository response
        when(aboutPageRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(content));

        // Call the service method
        AboutPageContentDto result = aboutPageService.getAboutPageContent();

        // Verify the result
        assertEquals("More Than Just A BoilerPlate", result.getTitle());
        assertEquals("Welcome to Hng Boilerplate, where passion meets innovation.", result.getIntroduction());

        // Verify the custom sections
        Map<String, Object> stats = (Map<String, Object>) result.getCustomSections().get("stats");
        assertEquals(10, stats.get("years_in_business"));
        assertEquals(75000, stats.get("customers"));
        assertEquals(100000, stats.get("monthly_blog_readers"));
        assertEquals(1200000, stats.get("social_followers"));

        Map<String, Object> services = (Map<String, Object>) result.getCustomSections().get("services");
        assertEquals("Trained to Give You The Best", services.get("title"));
        assertEquals("Since our founding, Hng Boilerplate has been dedicated to constantly evolving to stay ahead of the curve.", services.get("description"));
    }


    @Test
    public void testDeleteAboutPageContent() {
        // Prepare the mock entity
        AboutPageContent content = new AboutPageContent();
        content.setId(1L);

        // Mock repository response
        when(aboutPageRepository.findById(1L)).thenReturn(java.util.Optional.of(content));

        // Call the service method
        aboutPageService.deleteAboutPageContent();

        // Verify interactions with the repository
        verify(aboutPageRepository).delete(content);
    }


}
