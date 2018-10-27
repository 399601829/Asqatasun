/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2015  Asqatasun.org
 *
 * This file is part of Asqatasun.
 *
 * Asqatasun is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package org.asqatasun.webapp.controller;

import java.util.*;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import static org.easymock.EasyMock.*;
import org.asqatasun.emailsender.EmailSender;
import org.asqatasun.webapp.command.CreateUserCommand;
import org.asqatasun.webapp.command.factory.CreateUserCommandFactory;
import org.asqatasun.webapp.entity.service.user.RoleDataService;
import org.asqatasun.webapp.entity.service.user.UserDataService;
import org.asqatasun.webapp.entity.user.Role;
import org.asqatasun.webapp.entity.user.User;
import org.asqatasun.webapp.util.TgolKeyStore;
import org.asqatasun.webapp.validator.CreateUserFormValidator;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/**
 *
 * @author jkowalczyk
 */
public class SignUpControllerTest extends TestCase {
    
    private SignUpController instance;
    private CreateUserFormValidator createUserFormValidator;
    private UserDataService mockUserDataService;
    private User mockUser;
    private CreateUserCommand mockValidCreateUserCommand;
    private CreateUserCommand mockInvalidCreateUserCommand;
    private BindingResult mockInvalidBindingResult;
    private BindingResult mockValidBindingResult;
    Role mockUserRole;
    RoleDataService mockRoleDataService;
    
    public SignUpControllerTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instance = new SignUpController();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        if (mockUserDataService != null) {
            verify(mockUserDataService);
        }
        if (mockUser != null) {
            verify(mockUser);
        }
        if (mockInvalidBindingResult != null) {
            verify(mockInvalidBindingResult);
        }
        if (mockValidBindingResult != null) {
            verify(mockValidBindingResult);
        }
        if (mockUserRole != null) {
            verify(mockUserRole);
        }
        if (mockRoleDataService != null) {
            verify(mockRoleDataService);
        }
    }

    /**
     * Test of setUpSignUpPage method, of class SignUpController.
     */
    public void testSetUpSignUpPage() {
        System.out.println("setUpSignUpPage");
        
        Model model = new ExtendedModelMap();
        String expResult = TgolKeyStore.SIGN_UP_VIEW_NAME;
        String result = instance.setUpSignUpPage(model);
        // the returned view is the sign-up view name
        assertEquals(expResult, result);
        // the model contains a UserSignUpCommand instance to maps the data of
        // the form of the view
        assertTrue(model.asMap().get(TgolKeyStore.CREATE_USER_COMMAND_KEY) instanceof 
                CreateUserCommand);
    }

    /**
     * Test of setUpSignUpConfirmationPage method, of class SignUpController.
     */
    public void testSetUpSignUpConfirmationPage() {
        System.out.println("setUpSignUpConfirmationPage");
        Model model = new ExtendedModelMap();
        String expResult = TgolKeyStore.SIGN_UP_CONFIRMATION_VIEW_NAME;
        String result = instance.setUpSignUpConfirmationPage(model);
        // the returned view is the sign-up confirmation view name
        assertEquals(expResult, result);
        // the model contains a UserSignUpCommand instance to maps the data of
        // the form of the view
        assertTrue(model.asMap().get(TgolKeyStore.CREATE_USER_COMMAND_KEY) instanceof 
                CreateUserCommand);
    }

    /**
     * Create an valid instance (the email attribute is initialised) of
     * UserSignUpCommand
     * 
     * @return 
     */
    private CreateUserCommand createValidUserSignUpCommand() {  
        CreateUserCommand createUserCommand = new CreateUserCommand();
        createUserCommand.setEmail("test@test.com");
        createUserCommand.setSiteUrl("http://mysite.org");
        createUserCommand.setPassword("password");
        createUserCommand.setConfirmPassword("password");
        return createUserCommand;
    }
    
    /**
     * Create an invalid instance (the email attribute is not initialised) of
     * UserSignUpCommand
     * 
     * @return 
     */
    private CreateUserCommand createInvalidUserSignUpCommand() {
        return new CreateUserCommand();
    }
 
    
   private void setUpUserDataService() {
       mockUser = createMock(User.class);
       mockUser.setEmail1("test@test.com");
       expectLastCall();
       expect(mockUser.getEmail1()).andReturn("test@test.com").once();
       mockUser.setWebUrl1("http://mysite.org");
       expectLastCall();
       expect(mockUser.getWebUrl1()).andReturn("http://mysite.org").once();
       mockUser.setFirstName(null);
       expectLastCall();
       expect(mockUser.getFirstName()).andReturn(null).once();
       mockUser.setName(null);
       expectLastCall();
       expect(mockUser.getName()).andReturn(null).once();
       mockUser.setPhoneNumber(null);
       expectLastCall();
       expect(mockUser.getPhoneNumber()).andReturn(null).once();
       mockUser.setPassword("5f4dcc3b5aa765d61d8327deb882cf99");
       expectLastCall();
       mockUser.setAccountActivation(false);
       expectLastCall();
       mockUser.setRole(mockUserRole);
       expectLastCall();
       mockUserDataService = createMock(UserDataService.class);
       expect(mockUserDataService.create()).andReturn(mockUser).anyTimes();
       expect(mockUserDataService.getUserFromEmail("test@test.com")).andReturn(mockUser).anyTimes();
       expect(mockUserDataService.saveOrUpdate(mockUser)).andReturn(mockUser).anyTimes();
       
       replay(mockUser);
       replay(mockUserDataService);

       ReflectionTestUtils.setField(instance, "userDataService", mockUserDataService);
   }

   private void setUpValidatorAndBindingResult() {
       mockValidCreateUserCommand = createValidUserSignUpCommand();
       mockInvalidCreateUserCommand = createInvalidUserSignUpCommand();
       createUserFormValidator = new CreateUserFormValidator();
       createUserFormValidator.setUserDataService(mockUserDataService);
       mockInvalidBindingResult = createMock(BindingResult.class);
       mockValidBindingResult = createMock(BindingResult.class);
       createUserFormValidator.validate(mockValidCreateUserCommand, mockValidBindingResult);
       createUserFormValidator.validate(mockInvalidCreateUserCommand, mockInvalidBindingResult);
       
       expectLastCall();
       expect(mockValidBindingResult.hasErrors()).andReturn(false).once();
       expect(mockInvalidBindingResult.hasErrors()).andReturn(true).once();
       
       replay(mockValidBindingResult);
       replay(mockInvalidBindingResult);

       ReflectionTestUtils.setField(instance, "createUserFormValidator", createUserFormValidator);
   }
 
   private void setUpMockRoleDataService() {
        mockRoleDataService = createMock(RoleDataService.class);
        mockUserRole = createMock(Role.class);
         
        expect(mockRoleDataService.read(Long.valueOf(2))).andReturn(mockUserRole).anyTimes();
        expect(mockRoleDataService.read(Long.valueOf(3))).andReturn(null).anyTimes();
        expect(mockUserRole.getId()).andReturn(Long.valueOf(2)).anyTimes();

        replay(mockUserRole);        
        replay(mockRoleDataService);
        
        CreateUserCommandFactory.getInstance().setRoleDataService(mockRoleDataService);
        CreateUserCommandFactory.getInstance().initRoles();
    }

}