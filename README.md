## Automation - Selenium Test Suite

Maven-based regression checks for core shopping flows on the public demo storefront at https://automationexercise.com. The specs lean on Selenium WebDriver 4 plus TestNG, with explicit waits for the site's AJAX-heavy cart and signup steps.

---

### Prerequisites

- **Java 11** SDK (classpath matches `maven-compiler-plugin` `<release>`)
- **Apache Maven** 3.9+
- **Google Chrome** (stable channel—the suite launches `ChromeDriver` via Selenium Manager)

---

### Running the suite

```bash
mvn test
```

`mvn test` invokes Surefire against the `tests` package. Failures normally mean flaky network latency; if that happens bump the waits in `BaseTest`.

---

### Test inventory

1. `LoginTest.validLoginTest` — Valid credentials expose the authenticated header link.
2. `LoginTest.invalidLoginTest` — Bad password surfaces the site's standard error banner.
3. `RegisterTest.registerNewUserTest` — Random signup completes with the green “Account Created!” confirmation heading.
4. `RegisterTest.registerExistingEmailTest` — Reusing `test@saffiya.com` is rejected with duplicate-email copy.
5. `ProductTest.searchProductTest` — Querying *T-Shirt* renders the searched-products grid.
6. `ProductTest.viewProductDetailsTest` — Opening the first PDP shows name plus rupee pricing.
7. `CartTest.addToCartTest` — First SKU added through AJAX persists in `/view_cart`.
8. `CartTest.removeFromCartTest` — Removing the item restores the cart-empty helper banner.
9. `ContactFormTest.submitContactFormTest` — Attachment upload succeeds and shows the AJAX success pane.
10. `ContactFormTest.contactFormEmptySubmitTest` — HTML5 validation keeps interactions on `/contact_us`.

**Note.** `LoginTest.validLoginTest` and `RegisterTest.registerExistingEmailTest` assume the mailbox `test@saffiya.com` is already provisioned server-side—create it once manually if needed.

---

### Author

[Saffiya Sanhar](https://linkedin.com/in/saffiya-sanhar)
