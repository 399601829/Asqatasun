/*
 *  Asqatasun - Automated webpage assessment
 *  Copyright (C) 2008-2015  Asqatasun.org
 * 
 *  This file is part of Asqatasun.
 * 
 *  Asqatasun is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  Contact us by mail: asqatasun AT asqatasun DOT org
 */

package org.asqatasun.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.asqatasun.emailsender.EmailSender;
import org.asqatasun.entity.service.audit.AuditDataService;
import org.asqatasun.util.MD5Encoder;
import org.asqatasun.webapp.command.CreateContractCommand;
import org.asqatasun.webapp.command.CreateUserCommand;
import org.asqatasun.webapp.command.factory.CreateContractCommandFactory;
import org.asqatasun.webapp.command.factory.CreateUserCommandFactory;
import org.asqatasun.webapp.entity.contract.Act;
import org.asqatasun.webapp.entity.contract.Contract;
import org.asqatasun.webapp.entity.service.contract.ActDataService;
import org.asqatasun.webapp.entity.user.User;
import org.asqatasun.webapp.form.builder.FormFieldBuilder;
import org.asqatasun.webapp.form.parameterization.ContractOptionFormField;
import org.asqatasun.webapp.form.parameterization.builder.ContractOptionFormFieldBuilder;
import org.asqatasun.webapp.util.TgolKeyStore;
import org.asqatasun.webapp.validator.CreateContractFormValidator;
import org.asqatasun.webapp.validator.CreateUserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 *
 * @author jkowalczyk
 */
@Controller
public class AbstractUserAndContractsController extends AbstractController {

    @Resource(name = "formFieldBuilderList")
    List<FormFieldBuilder> displayOptionFieldsBuilderList;
    @Resource(name = "contractOptionFormFieldBuilderMap")
    protected Map<String, List<ContractOptionFormFieldBuilder>> contractOptionFormFieldBuilderMap;

    @Autowired
    private CreateUserFormValidator createUserFormValidator;
    @Autowired
    protected CreateContractFormValidator createContractFormValidator;
    @Autowired
    private ActDataService actDataService;
    @Autowired
    private AuditDataService auditDataService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    /**
     * 
     * @param model
     * @param user
     * @param successViewName
     * @return 
     */
    protected String prepateDataAndReturnCreateUserView(
            Model model,
            User user,
            String successViewName) {
        CreateUserCommand createUserCommand;
        if (user != null) {
            createUserCommand = CreateUserCommandFactory.getInstance().getInitialisedCreateUserCommand(user);
            model.addAttribute(TgolKeyStore.USER_NAME_KEY,createUserCommand.getEmail());
        } else {
            createUserCommand = CreateUserCommandFactory.getInstance().getNewCreateUserCommand();
        }
        model.addAttribute(TgolKeyStore.CREATE_USER_COMMAND_KEY,createUserCommand);
        return successViewName;
    }
    
    /**
     * 
     * @param model
     * @param user
     * @param contract
     * @param optionFormFieldMap
     * @param successViewName
     * @return 
     */
    protected String prepateDataAndReturnCreateContractView(
            Model model,
            User user,
            Contract contract,
            Map<String, List<ContractOptionFormField>> optionFormFieldMap, 
            String successViewName) {
        CreateContractCommand createContractCommand;

        if (contract != null) {
            createContractCommand = CreateContractCommandFactory.getInstance().getInitialisedCreateContractCommand(contract);
            model.addAttribute(TgolKeyStore.CONTRACT_NAME_KEY,createContractCommand.getLabel());
        } else {
            createContractCommand = CreateContractCommandFactory.getInstance().getNewCreateContractCommand();
        }
        if (user != null) {
            model.addAttribute(TgolKeyStore.USER_NAME_KEY,user.getEmail1());
            model.addAttribute(TgolKeyStore.USER_ID_KEY,user.getId());
        } else {
            model.addAttribute(TgolKeyStore.USER_LIST_KEY,getUserDataService().findAll());
        }
        model.addAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY,createContractCommand);
        model.addAttribute(TgolKeyStore.OPTIONS_MAP_KEY, optionFormFieldMap);

        return successViewName;
    }

    /**
     * A new user can be created from the main form that can be accessed without 
     * being authentified. In this case, we check the validity of the filled-in 
     * url and we prevent the new users to be activated and created with admin 
     * privileges.
     * On the other side, if the user is created from the admin interface, it can
     * be set with activation and admin privileges info but the check of the url
     * is useless cause the field has been removed from the form.
     * 
     * @param createUserCommand
     * @param result
     * @param model
     * @param successViewName
     * @param errorViewName
     * @param newUserFromAdmin
     * @param successMessageKey
     * @return
     * @throws Exception
     */
    protected String submitCreateUserForm (
            CreateUserCommand createUserCommand,
            BindingResult result,
            Model model,
            String successViewName,
            String errorViewName,
            boolean newUserFromAdmin,
            String successMessageKey) throws Exception {

        createUserFormValidator.setCheckSiteUrl(!newUserFromAdmin);
        // We check whether the form is valid
        createUserFormValidator.validate(createUserCommand, result);
        // If the form has some errors, we display it again with errors' details
        if (result.hasErrors()) {
            return displayFormWithErrors(
                    model,
                    createUserCommand,
                    errorViewName);
        }
        User user;
        if (!newUserFromAdmin) {
            user = createUser(createUserCommand,false,false);
        } else {
            user = createUser(createUserCommand,true,true);
            model.addAttribute(TgolKeyStore.USER_LIST_KEY,getUserDataService().findAll());
            model.addAttribute(successMessageKey,user.getEmail1());
        }
        return successViewName;
    }
    
    /**
     * This methods controls the validity of the form and updated the user 
     * 
     * @param createUserCommand
     * @param result
     * @param request
     * @param model
     * @param userToModify
     * @param successViewName
     * @param errorViewName
     * @param updateAllUserData
     * @param updateUserFromAdmin
     * @param successMessageKey
     * @return
     * @throws Exception
     */
    protected String submitUpdateUserForm (
            CreateUserCommand createUserCommand,
            BindingResult result,
            HttpServletRequest request,
            Model model,
            User userToModify,
            String successViewName,
            String errorViewName,
            boolean updateAllUserData,
            boolean updateUserFromAdmin,
            String successMessageKey)
            throws Exception {

        // We check whether the form is valid
        createUserFormValidator.validateUpdate(createUserCommand, result, userToModify.getEmail1());
        // If the form has some errors, we display it again with errors' details
        if (result.hasErrors()) {
            return displayFormWithErrors(
                    model,
                    createUserCommand,
                    errorViewName);
        }
        User user = updateUser(
                createUserCommand,
                userToModify,
                updateAllUserData, 
                updateUserFromAdmin);

        // when updated from admin page, the id of the user to modify is passed
        // through the session and needs to be cleaned up once updated.
        if (updateUserFromAdmin) {
            model.addAttribute(TgolKeyStore.USER_LIST_KEY,getUserDataService().findAll());
            request.getSession().removeAttribute(TgolKeyStore.USER_ID_KEY);
        }
        if (successMessageKey != null) {
            model.addAttribute(successMessageKey,user.getEmail1());
        }
        return successViewName;
    }
    
    /**
     * Create a user entit
     * @param createUserCommand
     * @return
     * @throws Exception
     */
    private User createUser(
            CreateUserCommand createUserCommand,
            boolean allowActivation,
            boolean allowAdmin) throws Exception {
        User user = getUserDataService().create();
        user.setEmail1(createUserCommand.getEmail());
        user.setFirstName(createUserCommand.getFirstName());
        user.setName(createUserCommand.getLastName());
        user.setPhoneNumber(createUserCommand.getPhoneNumber());
        user.setPassword(MD5Encoder.MD5(createUserCommand.getPassword()));
        user.setWebUrl1(createUserCommand.getSiteUrl());
        if (allowActivation) {
            user.setAccountActivation(createUserCommand.getActivated());
        } else {
            user.setAccountActivation(false);
        }
        if (allowAdmin && createUserCommand.getAdmin()) {
            user.setRole(CreateUserCommandFactory.getInstance().getAdminRole());
        } else {
            user.setRole(CreateUserCommandFactory.getInstance().getUserRole());
        }
        getUserDataService().saveOrUpdate(user);
        return user;
    }

    /**
     * Update a user entity
     * 
     * @param createUserCommand
     * @param user
     * @param updateAllUserData
     * @param updateUserFromAdmin
     * @return
     * @throws Exception 
     */
    private User updateUser(
            CreateUserCommand createUserCommand,
            User user,
            boolean updateAllUserData,
            boolean updateUserFromAdmin) throws Exception {
        boolean hasSessionToBeUpdated = true;
        if (updateUserFromAdmin && !user.getEmail1().equals(getAuthenticatedUsername())) {
            hasSessionToBeUpdated = false;
        }
        if (updateAllUserData) {
            user.setEmail1(createUserCommand.getEmail());
        }
        user.setFirstName(createUserCommand.getFirstName());
        user.setName(createUserCommand.getLastName());
        user.setPhoneNumber(createUserCommand.getPhoneNumber());
        if (updateAllUserData) {
            user.setAccountActivation(createUserCommand.getActivated());
            if (createUserCommand.getAdmin()) {
                user.setRole(CreateUserCommandFactory.getInstance().getAdminRole());
            } else {
                user.setRole(CreateUserCommandFactory.getInstance().getUserRole());
            }
        }
        getUserDataService().saveOrUpdate(user);
        // the current user accessible from the session needs also to be updated
        if (hasSessionToBeUpdated) {
            updateCurrentUser(user);
        }
        return user;
    }
    
    /**
     * 
     * @param model
     * @param createUserCommand
     * @param errorViewName
     * @return
     */
    private String displayFormWithErrors(
            Model model,
            CreateUserCommand createUserCommand,
            String errorViewName) {
        model.addAttribute(TgolKeyStore.CREATE_USER_COMMAND_KEY,
                createUserCommand);
        return errorViewName;
    }
    
    /**
     * 
     * @param model
     * @param createContractCommand
     * @param userName
     * @param userId
     * @param optionFormFieldMap
     * @param errorViewName
     * @return 
     */
    public String displayFormWithErrors(
            Model model,
            CreateContractCommand createContractCommand,
            String userName,
            Long userId,
            Map<String, List<ContractOptionFormField>> optionFormFieldMap, 
            String errorViewName) {
        model.addAttribute(TgolKeyStore.CREATE_CONTRACT_COMMAND_KEY,
                CreateContractCommandFactory.getInstance().getInitialisedCreateContractCommand(createContractCommand));
        model.addAttribute(TgolKeyStore.USER_NAME_KEY,userName);
        model.addAttribute(TgolKeyStore.USER_ID_KEY,userId);
        model.addAttribute(TgolKeyStore.OPTIONS_MAP_KEY, optionFormFieldMap);
        // case : create contract for multiple users
        if (userName == null && userId == null) {
            model.addAttribute(TgolKeyStore.USER_LIST_KEY, getUserDataService().findAll());
        }
        return errorViewName;
    }


    /**
     * @param user
     * @return whether the current authenticated user is admin
     */
    protected boolean isUserAdmin(User user) {
        return user.getRole().getId().equals(
                CreateUserCommandFactory.getInstance().getAdminRole().getId());
    }

    /**
     * @return whether the current authenticated user is admin
     */
    protected boolean isCurrentUserAdmin() {
        return isUserAdmin(getCurrentUser());
    }

    /**
    * 
    * @param contract 
    */
    protected void deleteAllAuditsFromContract(Contract contract) {
        Collection<Act> actsByContract = actDataService.getAllActsByContract(contract);
        for (Act act : actsByContract) {
            if (act.getAudit() != null) {
                auditDataService.delete(act.getAudit().getId());
            }
            actDataService.delete(act.getId());
        }
    }

    

}