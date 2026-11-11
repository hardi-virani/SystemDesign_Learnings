<div align="center">

# 📒 Mistakes Log

**Every bug I made while practising LLD, why it was wrong, and the rule that stops it happening again.**

![Mistakes](https://img.shields.io/badge/Mistakes_logged-3-D9951A?style=for-the-badge)
![Repeats](https://img.shields.io/badge/Repeated-1-C0392B?style=for-the-badge)

[⬅ Back to the study plan](README.md)

</div>

---

## 📋 At a glance

| # | Mistake | Topic | Seen | Status |
|:-:|:--|:--|:-:|:-:|
| 1 | [Storing a name `String` instead of the actual object](#1-storing-a-name-string-instead-of-the-actual-object) | Class relationships | 🔁 **2×** | 🔴 Watch closely |
| 2 | [`list.remove(x)` with the wrong type](#2-listremovex-with-the-wrong-type) | Collections | 1× | 🟡 Learned |
| 3 | [A getter changing a stored value](#3-a-getter-changing-a-stored-value) | Encapsulation | 1× | 🟡 Learned |

**Status key:** 🔴 repeated, check for it every time · 🟡 made once, rule learned · 🟢 not repeated for 2+ weeks

> [!TIP]
> Read the **Rule** lines before every practice session and before each mock interview. On review days, ask: did any of these come back?

---

## 1. Storing a name `String` instead of the actual object

`Class relationships` · Seen **2 times** · Last seen **6 Nov 2026**

> [!CAUTION]
> **Repeated mistake.** This is the second time. Check every field that holds a name or ID.

**What went wrong:** a class kept the *name* of a related object instead of a reference to the object itself.

❌ **Wrong**

```java
class Course {
    private String professorName;

    void setProfessor(String name) {
        this.professorName = name;
    }
}
```

✅ **Right**

```java
class Course {
    private Professor professor;

    void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
```

**Why it matters:**
- A `String` is not a relationship. `Course` can no longer reach the professor's other data or methods.
- Two professors with the same name can't be told apart.
- If the professor's name changes, the course keeps the old one.

> [!IMPORTANT]
> **Rule:** if the domain says "a Course *has* a Professor", the field type is `Professor`, not `String`.

---

## 2. `list.remove(x)` with the wrong type

`Collections` · Seen **1 time** · Last seen **6 Nov 2026**

**What went wrong:** called `remove` on a `List<Room>` with a `String`. It compiles, because `remove` takes any `Object`, but nothing is removed.

❌ **Wrong**

```java
List<Room> rooms = new ArrayList<>();

// compiles, returns false,
// the room stays in the list
rooms.remove("Powder Room");
```

✅ **Right**

```java
// match on a field
rooms.removeIf(r ->
    r.getName().equals("Powder Room"));

// or pass the same Room object
rooms.remove(powderRoom);
```

**Why it matters:**
- `remove(Object)` compares using `equals()`. A `String` is never equal to a `Room`, so it silently does nothing.
- No error and no exception, so the bug is easy to miss.
- Related trap: on a `List<Integer>`, `list.remove(1)` removes **index 1**, not the value `1`. Use `list.remove(Integer.valueOf(1))` for the value.

> [!IMPORTANT]
> **Rule:** pass `remove` an object of the list's own type. To remove by a field, use `removeIf`.

---

## 3. A getter changing a stored value

`Encapsulation` · Seen **1 time** · Last seen **6 Nov 2026**

**What went wrong:** a `get...()` method also changed a field, so reading the value changed it.

❌ **Wrong**

```java
public double getTotalArea() {
    for (Room r : rooms) {
        totalArea += r.getArea();
    }
    return totalArea;   // grows on every call
}
```

✅ **Right**

```java
public double getTotalArea() {
    double total = 0;   // local variable
    for (Room r : rooms) {
        total += r.getArea();
    }
    return total;       // same answer every call
}
```

**Why it matters:**
- Calling the getter twice gives two different answers.
- Printing or debugging the value changes the program's state.
- Other code expects getters to be safe to call any number of times.

> [!IMPORTANT]
> **Rule:** a getter only reads. If it needs to calculate, use a local variable. Change fields only in methods named for the change (`add...`, `set...`, `update...`).

---

## ➕ Adding a new mistake

Copy this block, fill it in, and add a row to the table at the top. If a mistake comes back, raise its **Seen** count, update **Last seen**, and set its status to 🔴.

````markdown
## N. Short name of the mistake

`Topic` · Seen **1 time** · Last seen **DD Mon YYYY**

**What went wrong:** one sentence.

(wrong and right code side by side)

**Why it matters:**
- ...

> [!IMPORTANT]
> **Rule:** one line you can remember in an interview.
````
