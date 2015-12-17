package org.hazehorizon.restaurantPicker;

import java.io.IOException;

import javax.servlet.Filter;

import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractDto;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test.properties")
@ContextConfiguration(classes = { RestaurantPicker.class })
public abstract class AbstractIntegrationTest
{
	protected static int DEFAULT_STING_LEN = 32;

	protected static String SYSTEM_LOGIN = "system";
	protected static String SYSTEM_ROLE = "SYSTEM";
	protected static int SYSTEM_ID = 1;
	protected static String ADMIN_LOGIN = "admin";
	protected static String ADMIN_ROLE = "ADMIN";
	protected static int ADMIN_ID = 2;
	protected static long RESTAURANT_ID = 1;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    private MockMvc mvc;


    @Before
    public final void setUpOfMocks() throws Exception
    {
        mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
    }

    protected MockMvc mvc()
    {
        return mvc;
    }

    /*    private JdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(final DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }*/
    
    /*    	
	(UserEntity)jdbc().queryForObject("select * from user_entity where login = ?", new BeanPropertyRowMapper(UserEntity.class), user.getLogin());
	assertEquals(user.getLogin(), dbUser.getLogin());
	assertEquals(user.getPasswd(), dbUser.getPasswd());
	assertEquals(user.getActive(), dbUser.isActive());
	List<Map<String, Object>> rows = jdbc().queryForList("select * from user_entity_roles");
	
	RowCountCallbackHandler countCallback = new RowCountCallbackHandler();
	jdbc().query("select * from user_entity_roles where user_entity_id = ?", countCallback, dbUser.getId());
	assertEquals(user.getRoles().size(), countCallback.getRowCount());*/

    protected String toJson(AbstractDto<?> dto) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(dto);
    }
    
    protected <T extends AbstractDto<?>> T toDto(Class<T> type, String json) throws JsonMappingException, JsonParseException, IOException
    {
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.registerModule(new JavaTimeModule());
    	ResponseWrapper<?> wrapper = mapper.readValue(json, ResponseWrapper.class);
    	return mapper.convertValue(wrapper.getData(), type);
    }
}
