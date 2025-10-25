# AI Declaration - Assignment 2

**Student Name:** Ahmad Danindra Nugroho
**Student Number:** s4878684
**Course:** CSSE2002 - Programming in the Large  
**Assignment:** Assignment 2
**Semester:** Semester 2, 2025

---

## Declaration

I declare that I have used Generative AI tools to assist with this assignment in the following ways:

### AI Tool Used
- **Tool Name:** Claude AI (Anthropic)
- **Version:** Claude 4.5 Sonnet

---

## How AI Was Used

### 1. **Understanding Assignment Requirements**
- Used AI to help interpret the assignment specification and clarify requirements
- Asked questions about refactoring scope and allowed packages to modify
- Discussed software design principles (DRY, SOLID, etc.) and how to apply them

### 2. **Identifying Code Smells and Refactoring Opportunities**
- Used AI to help identify code duplication in spawner classes
- Discussed issues with information hiding and encapsulation in manager classes
- Analyzed instanceof usage and discussed polymorphism alternatives

### 3. **Refactoring Design and Implementation**
- Collaborated with AI to design the `AbstractEnemySpawner` base class
- Discussed pros and cons of different refactoring approaches
- Received guidance on applying Template Method pattern
- Got suggestions for improving method naming and documentation

### 4. **Code Generation**
AI generated code for the following refactored classes:
- `AbstractEnemySpawner.java` - Complete implementation
- `MagpieSpawner.java` - Refactored version extending abstract class
- `EagleSpawner.java` - Refactored version extending abstract class
- `PigeonSpawner.java` - Refactored version extending abstract class
- `EnemyManager.java` - Refactored with improved encapsulation
- Updated methods in `BeeHive.java`, `GuardBee.java`, `Scarecrow.java`

**Important Note:** All AI-generated code was:
- Reviewed and understood by me before use
- Tested to ensure it works correctly and passes all system tests
- Modified as needed to fit the specific requirements
- Integrated incrementally with continuous testing

### 5. **Documentation Assistance**
- AI helped structure and write the README.md documentation
- Assisted with organizing refactoring explanations
- Suggested improvements to Javadoc comments
- Helped articulate which design principles were applied

### 6. **Debugging and Problem-Solving**
- Used AI to discuss compilation errors during refactoring
- Explored why certain refactorings broke tests
- Discussed backward compatibility issues (e.g., public fields in NpcManager)
- Analyzed test failures and their root causes

---

## What AI Did NOT Do

To clarify the boundaries of AI usage:

1. **Bug Fixing:** The original bugs in the codebase (Magpie movement, bee spawning, etc.) were identified and would need to be fixed by me independently. AI discussions helped understand the problems but did not provide complete bug fixes.

2. **Test Writing:** Unit tests for the refactored classes would need to be written by me.

3. **Design Decisions:** While AI provided suggestions and discussed trade-offs, all final decisions about which refactorings to implement and how to prioritize them were made by me.

4. **Understanding Verification:** I verified my understanding of all code by:
   - Reviewing generated code line by line
   - Running tests after each change
   - Ensuring I could explain how and why the code works
   - Making modifications when needed

---

## My Original Contributions

Beyond the AI-assisted work, my contributions include:

1. **Strategic Refactoring Planning:**
   - Decided which refactorings to prioritize
   - Determined the order of refactorings (spawners first, then managers)
   - Chose to proceed incrementally with continuous testing

2. **Integration and Testing:**
   - Integrated all refactored code into the existing codebase
   - Ran system tests after each refactoring step
   - Debugged integration issues
   - Ensured backward compatibility

3. **Code Review and Quality Assurance:**
   - Reviewed all AI-generated code for correctness
   - Verified that refactorings maintained system test compliance
   - Ensured code style consistency
   - Made adjustments where AI suggestions didn't fit the context

4. **Version Control:**
   - Managed Git commits with descriptive messages
   - Tracked progress through version control
   - Maintained clean commit history

5. **Critical Thinking:**
   - Evaluated when to accept vs. modify AI suggestions
   - Recognized limitations (e.g., when public fields couldn't be made private)
   - Balanced ideal design with practical constraints

---

## Learning Outcomes

Through this AI-assisted approach, I have:

1. **Deepened Understanding of Design Patterns:**
   - Learned how Template Method pattern works in practice
   - Understood when and why to use abstract classes vs interfaces
   - Recognized situations where polymorphism improves code

2. **Improved Refactoring Skills:**
   - Learned to identify code smells systematically
   - Practiced incremental refactoring with continuous testing
   - Understood importance of backward compatibility

3. **Enhanced Software Design Knowledge:**
   - Applied SOLID principles in real codebase
   - Practiced DRY principle through abstraction
   - Learned about information hiding and encapsulation trade-offs

4. **Developed Critical Evaluation Skills:**
   - Learned to evaluate AI suggestions critically
   - Understood when to modify or reject AI-generated code
   - Balanced theoretical ideals with practical constraints

---

## Ethical Considerations

I acknowledge that:

1. All code submissions are my responsibility, regardless of AI assistance
2. I have reviewed and understood all AI-generated code
3. I can explain and defend all refactoring decisions made
4. This declaration honestly represents how AI was used in my work
5. I followed UQ's academic integrity policies regarding AI usage

---

## Conclusion

Generative AI was used as a collaborative tool to enhance my learning and productivity, similar to how one might consult with a tutor, textbook, or online resources. The final submission represents my understanding, decisions, and integration work, with AI serving as an educational assistant throughout the process.

I take full responsibility for all code submitted and confirm that I understand how it works and why the refactorings improve code quality.

---
