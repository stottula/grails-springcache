/**
 * Generated by the Shiro plugin. This filters class protects all URLs
 * via access control by convention.
 */
class SecurityFilters {
    def filters = {
        all(controller: "user") {
            before = {
                accessControl()
            }
        }
    }
}