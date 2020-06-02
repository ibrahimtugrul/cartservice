package com.ibrahimtugrul.cartservice.infrastructure.locale;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageSourceLocalizer {

    private final ResourceBundleMessageSource messageSource;

    public String getLocaleMessage(final String key, final Object... args) {
        try {
            final Locale locale = retrieveLocale();
            return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("{} not found in messages file", key, e);
            return StringUtils.EMPTY;
        }
    }

    private Locale retrieveLocale() {
        return LocaleContextHolder.getLocale();
    }
}