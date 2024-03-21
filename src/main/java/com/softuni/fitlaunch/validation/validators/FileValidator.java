package com.softuni.fitlaunch.validation.validators;

import com.softuni.fitlaunch.validation.anotations.FileAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.softuni.fitlaunch.commons.ErrorMessages.EXCEEDED_FILE_SIZE;
import static com.softuni.fitlaunch.commons.ErrorMessages.FILE_MUST_BE_PROVIDED;
import static com.softuni.fitlaunch.commons.ErrorMessages.INVALID_FILE_EXTENSION;

public class FileValidator implements ConstraintValidator<FileAnnotation, MultipartFile> {

    private List<String> contentTypes;
    private long size;

    @Override
    public void initialize(FileAnnotation constraintAnnotation) {
        this.size = constraintAnnotation.size();
        this.contentTypes = Arrays.stream(constraintAnnotation.contentTypes()).toList();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        String errorMsg = getErrorMsg(file);

        if (!errorMsg.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMsg)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    private String getErrorMsg(MultipartFile file) {
        if (file.isEmpty()) {
            return FILE_MUST_BE_PROVIDED;
        }

        if (file.getSize() > size) {
            return String.format(EXCEEDED_FILE_SIZE, size);
        }

        if (!contentTypes.contains(file.getContentType())) {
            return String.format(INVALID_FILE_EXTENSION, String.join(", ", contentTypes));
        }

        return "";
    }
}
