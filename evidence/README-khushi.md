# Member 2 implementation notes

Member 2's customer-side services are implemented in an in-memory form, using property IDs so they can connect to the team's shared Property model without duplicating it.


## Included
- User registration, email uniqueness, login/logout, profile updates, password changes, role field, and SHA-256 password hashing.
- Customer favorites, recently viewed IDs, and comparison selection (duplicate IDs are prevented).
- Inquiry submission/history/cancellation for pending inquiries.
- Property tour request creation and user request history.
- Input validation with `Member2ValidationException`.


## Integration notes
- Data is currently stored in memory and resets when the process exits. JDBC/DAO persistence remains a later integration step.
- Property references are IDs (`String`) to avoid creating or changing Member 1's `Property` class.
- Tour requests are collected but no concurrency, capacity, or scheduling policy is implemented; coordinate processing with Member 3.
- Services are not wired into `Main.java`, intentionally preserving the shared entry point for Member 1.
