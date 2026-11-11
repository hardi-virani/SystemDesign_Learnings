# Practice Mistake Log

## Recurring patterns

Category counts (all rounds):
- [Comprehension]: 2
- [Debugging]: 1
- [Prompting]: 2
- [Validation]: 3
- [Communication]: 1
- [Time management]: 2
- [Fundamentals]: 1

Mistakes seen in 2+ rounds:
- None yet (only 1 round logged).

Progress since last round:
- N/A (first round).

---

### Round 1 | micrograd | 2026-11-07 | Fallible mode: ON
**Result:** bugs fixed 2/3, feature not started, optimization not started
**Mistakes:**
- [Comprehension] Decided not to read `engine.py` (95 lines) and `nn.py` (60 lines) because reading the whole file "would take too long", and handed understanding to the AI -> skim both core files top to bottom first (about 4 minutes) and write one line per class/method on what it does.
- [Comprehension] Never opened `test/`, so didn't know what "correct" meant (comparison against PyTorch) or where regression tests go -> skim the existing tests during orientation, before touching any ticket.
- [Time management] Ignored the ready virtualenv at `../.venv-micrograd` named in the setup section, installed PyTorch manually, then lost time on Python SDK config, Jupyter and scikit-learn -> read the setup section fully and use the provided environment. Run the given test command first.
- [Time management] Ticket #103 not attempted and Phases 2 and 3 never started. Most of the 60 minutes went to environment setup and the #102 back-and-forth -> time-box each ticket (about 8 minutes in Phase 1) and move on, leaving a written hypothesis if stuck.
- [Debugging] Ticket #101: formed no hypothesis even though the report said gradients were wrong "especially ones I reuse in several places", a direct hint at gradient accumulation (`=` vs `+=`). Pasted the whole ticket and asked the AI to find the bug -> state a hypothesis ("a `_backward` overwrites `.grad` instead of accumulating") and ask the AI to confirm or refute it.
- [Prompting] P1: "Now Just visit engine.py file and there is some calculation mistake in this file which is giving b.grad wrong answer. so help me solve that." -> "b.grad is wrong but a.grad is right, and reused variables are affected. Check each `_backward` in engine.py for `=` vs `+=` and tell me which one, without editing."
- [Prompting] P2: "...help me solve this bug. And in nn.py the last class model needs improvment." Said aloud that you had a prediction, but it never made it into the prompt, and "needs improvement" gave no target -> "Output is always >= 0, so I suspect the final layer applies ReLU. Walk through the `nonlin` expression in `MLP.__init__` for nouts=[16,16,1] and tell me which layers get nonlin=True."
- [Validation] Applied the AI's #102 fix `nonlin=i!=len(sz)-1` to the file without reading it closely. Since `sz = [nin] + nouts`, `len(sz)-1 == len(nouts)`, so it was a no-op -> substitute concrete values into any suggested expression before applying it (5 seconds here).
- [Validation] Reported "the rest is shown correctly, from min 0 to max 2.5343453" when a minimum of exactly 0 was itself the bug; the check script said the minimum should be negative -> compare every observed number against the stated expectation, not just the one you were looking for.
- [Validation] No regression test added for #101 or #102, though the task asks for one per ticket -> after each fix, add a small test (for example, a reused-variable gradient check against PyTorch; assert the last MLP layer is linear) before moving on.
- [Communication] Thinking aloud was mostly about what to ask the AI ("I'll let the AI generate it, then review the difference"), not about the code. Never stated either root cause in your own words -> after each fix, say the root cause in one sentence and the trade-off of the fix.
- [Fundamentals] Struggled to run the edited `nn.py` (no main block, unsure whether Jupyter was required, running from the wrong directory) -> practice running a package module from the repo root (`python -c "from micrograd.nn import MLP; print(MLP(2,[16,16,1]))"`) and using a project venv.
**Caught AI mistakes:** 1 of 1 (partial: noticed the last layer still printed ReLUNeuron by running the check, but did not catch it before applying, and the assistant explained why the fix was wrong)
**Focus for next round:** Before every debugging prompt, write one sentence of hypothesis taken from the symptom, and put it in the prompt.
